import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { first } from 'rxjs/operators';

import { propiedades_globales as prop_glo } from 'src/app/globals';
import { Status } from 'src/app/services/data/status-const';
import { AuthService } from 'src/app/services/auth/auth.service';
import { StoreService } from 'src/app/services/data/store.service';
import { ProductService } from 'src/app/services/data/product.service';
import { SaleService } from 'src/app/services/data/sale.service';
import { Sale } from 'src/app/model/data/sale';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';
import { DetailSale } from 'src/app/model/data/detail-sale';

@Component({
    selector: 'app-form-sale',
    templateUrl: './form-sale.component.html',
    styleUrls: ['./form-sale.component.css']
 })

export class FormSaleComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private authService: AuthService,
    private serviceUse: SaleService,
    private storeService: StoreService,
    private productService: ProductService,
    private location: Location,
    private notificationService:NotificationsService,
    public translate: TranslateService
  ) {
    translate.addLangs(prop_glo.info_globals.idiomas.config);
    translate.setDefaultLang(prop_glo.info_globals.idiomas.default);
  }

  public submitted: boolean = false;
  public progressing: boolean = false;
  public isCreateMode: boolean = false;
  public isViewMode: boolean = false;
  public isDeleteMode: boolean = false;

  public label_text: any = prop_glo.label_component;
  public label_error: any = prop_glo.sms_error_component;
  public label_btn: any = prop_glo.label_btn;
  public status_activo: any = Status.estados[0];

  public form!: FormGroup;
  public listStore: any;
  public listProduct: any; 
  public id: any = null;   
  public info_component!: any; 
  public form_data: any;

  public img_sale_default = prop_glo.info_globals.pages_url_base_img.concat(prop_glo.info_globals.default_img);
 
  ngOnInit(): void {     
    this.form = this.formBuilder.group(
      {
        quantity: [{ value: "", disabled: this.isViewMode }, Validators.required], 
        productId: [{ value: "undefined", disabled: this.isViewMode }, [Validators.required]], 
        storeId: [{ value: "undefined", disabled: this.isViewMode }, [Validators.required]],
        statusId: [{ value: this.status_activo.id, disabled: this.isViewMode }, null],
       }
    );
    this.getInfoComponent();
  }

  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    this.controlLoading (true);

    this.form_data = this.form.value;   

    let objSaleDetail:DetailSale = {
      cant:this.form_data.quantity,
      productId: this.form_data.productId
    };
 
    let sale_data = new Sale( null, this.form_data.storeId, Array.of(objSaleDetail) );
    
    console.log(sale_data);

    if (! this.isCreateMode) {  
      this.updateProduct(sale_data);
    }else{
      this.saveProduct(sale_data);
    } 

  }
 
  saveProduct(sale_data:Sale){
    this.serviceUse.save(sale_data).subscribe(
      (response: any) => {

        let sms: string, pref: string;
        this.authService.setToken(response.token);
        let existeError: boolean = response.error != null && response.error != '';
      
        if (existeError) {
          sms = response.error;
          pref = prop_glo.sms_error_component.pref_error;
        } else {
          sms = this.info_component.owner.concat(' ').concat(prop_glo.sms_component.sms_success_add);
          pref = prop_glo.sms_component.pref_exito;
        }

        this.postExecuteNotification(existeError, sms, pref);
      }, error => { console.log(error); }
    );
  }

  updateProduct(sale_data:Sale): void {  
    this.serviceUse.update(this.id, sale_data).subscribe(
      (response: any) => {

        let sms: string, pref: string;
        this.authService.setToken(response.token);
        let existeError: boolean = response.error != null && response.error != '';
         
        if (existeError) {
          sms = response.error;
          pref = prop_glo.sms_error_component.pref_error;
        } else {
          sms = this.info_component.owner.concat(' ').concat(prop_glo.sms_component.sms_success_edit);
          pref = prop_glo.sms_component.pref_exito;
        }

        this.postExecuteNotification(existeError, sms, pref);  

      },
      error => {
        console.log(error);
      });
  }

  onDelete() {
    this.serviceUse.delete(this.id).subscribe(
      (response: any) => {
        let sms: string, pref: string;
        this.authService.setToken(response.token);
        let existeError: boolean = response.error != null && response.error != '';

        if (existeError) {
          sms = response.error;
          pref = prop_glo.sms_error_component.pref_error;
        } else {
          sms = this.info_component.owner.concat(' ').concat(prop_glo.sms_component.sms_success_edit);
          pref = prop_glo.sms_component.pref_exito;
        }

        this.postExecuteNotification(existeError, sms, pref);
      }, error => { console.log(error); }
    );

  }

  getIdParams(): Number {
    let id_param: Number = 0;
    this.route.paramMap.subscribe((params: ParamMap) => {
      id_param = Number(params.get('id') || null);
    });
    return id_param;
  }

  findItemById(id: any): void {
    this.serviceUse.findOne(id).pipe(first()).subscribe(
      (response: any) => {
        console.log(response);    
        this.authService.setToken(response.token);
        let existeError = response.error != null && response.error != '';

        if (existeError) {
          console.log(response.error);
        } else {
          this.form.patchValue(response.info);
          this.controlLoading (false); 
        }
      },
      error => {
        console.log(error);
      });
  }

  getSelectedAddStock(): void {
    this.productService.findAllOptions().subscribe((data: any) => {
      this.authService.setToken(data.token);
      this.listProduct = data.info.content;
    }, (error: any) => {
      console.log(error);
    });

    this.storeService.findAllOptions().subscribe((data: any) => {
      this.authService.setToken(data.token);
      this.listStore = data.info.content;

      this.controlLoading (false); 

    }, (error: any) => {
      console.log(error);
    });

  }

  getInfoComponent() {   
    this.controlLoading (true);   
    this.getSelectedAddStock();    

    let ruta = this.router.url;
    let owner = ruta.split('/')[1];
    this.info_component = this.serviceUse.getInfoComponent(ruta, owner);
    let actions = prop_glo.info_globals.acciones_component.all_action;

    this.isViewMode = this.info_component.accion_activa == actions[2];//actions[4] = view ~
    this.isCreateMode = this.info_component.accion_activa == actions[3]; //actions[4] = create ~
    this.isDeleteMode = this.info_component.accion_activa == actions[1]; //actions[1] = delete ~
    
    if (!this.isCreateMode) {  //actions[4] = create ~   
      this.id = this.getIdParams();
      console.log("ITEM [" + owner + "] | ID SELECCIONADO:" + this.id);
      this.findItemById(this.id);
    } 
  }
      
  postExecuteNotification(_existeError: boolean, sms: string, pref: string) {
    if (_existeError) { 
      this.controlLoading (false); 
      this.toastr.error(sms, pref, {
        timeOut: 2000, positionClass: 'toast-top-center'
      });
      
    } else {
      this.notificationService.useCache = false;
      
      this.toastr.success(sms, pref, {
        timeOut: 3000, positionClass: 'toast-top-center'
      }).onHidden.subscribe( () => { this.onReset();  this.goUpdatedList();} );
    } 
    
  }
  /* Metodo utilitario */
  onReset(): void {
    this.submitted = false;  
    this.form.reset();
  }

  /* Metodos de navegacion */
  reloadCurrentPage() {
    window.location.reload();
  }

  goUpdatedList() {
    this.router.navigate([this.info_component.owner]);
  }

  goBack(): void {
    this.location.back();
  }

  controlLoading (status : boolean) : void {
    this.notificationService.setVisualizeLoading(status); //notificamos si necesitamos o no mostrar el loading
    this.progressing = status; //esta variable es usada para indicar que se procesa alguna peticion.
  }
}