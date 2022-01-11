import { Component, OnInit } from '@angular/core';
import { Router} from '@angular/router';
import { Location } from '@angular/common';  
import { AuthService } from 'src/app/services/auth/auth.service';
import { propiedades_globales as prop_glo } from 'src/app/globals'; 
import { TranslateService } from '@ngx-translate/core';
import { TagService } from 'src/app/services/data/tag.service'; 
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

@Component({
  selector: 'app-tag',
  templateUrl: './tag.component.html',
  styleUrls: ['./tag.component.css']
})
export class TagComponent implements OnInit{   
  public progressing: boolean = false; 
  public use_cache: boolean = true;
  public maskPhone: string = prop_glo.mask.mask_phone;    
  
  public label_btn: any = prop_glo.label_btn;
  public label_text: any = prop_glo.label_component;
  public info_component: any =  prop_glo.info_globals.info_component;
 
  constructor(
    private router: Router, 
    private authService: AuthService,
    private serviceUse: TagService,
    private location: Location,
    private notificationService:NotificationsService,
    public translate: TranslateService
    ) {
      translate.addLangs(prop_glo.info_globals.idiomas.config);
      translate.setDefaultLang(prop_glo.info_globals.idiomas.default);
    } 
 
  ngOnInit(): void { 
    this.getAllTags(); 
  }
  
  getAllTags(): void {
    this.controlLoading(true);
    
    this.restInfoComponent(); 
    this.use_cache = this.notificationService.useCache == undefined;
    this.serviceUse.findAll(this.use_cache).subscribe((data: any) => {
      this.authService.setToken(data.token);
      this.getInfoComponent(data);  
    }, error => {
      console.log(error);
    });
  }
 
  getInfoComponent(data: any) : void {
    let ruta = this.router.url;
    let owner = ruta.split('/')[1];     
    
    this.info_component = this.serviceUse.getInfoComponent(ruta, owner); 
    this.info_component.count_item = data.info.totalElements;    
    this.info_component.empty  = data.info.empty;

    if (data.info.empty) { 
      this.info_component.sms_empty  = this.label_text.list_empty;
    } else {
      this.info_component.list.data = data.info.content;
    }
    
    this.info_component.list.header_item = this.serviceUse.getTableHeaderName(data.info.content);  
    this.controlLoading(false);
  }

  restInfoComponent() : void {   
    this.use_cache = true;
    this.info_component.title = '';   
    this.info_component.list.data = '';
    this.info_component.list.acciones_crud = [];
  }
  
  goBack(): void {
    this.location.back();
  } 

  controlLoading (status : boolean) : void {
    this.notificationService.setVisualizeLoading(status); //notificamos si necesitamos o no mostrar el loading
    this.progressing = status; //esta variable es usada para indicar que se procesa alguna peticion.
  }
}