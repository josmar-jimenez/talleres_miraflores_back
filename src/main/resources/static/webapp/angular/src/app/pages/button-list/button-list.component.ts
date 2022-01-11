import { Component, OnInit, Input, OnDestroy, OnChanges } from '@angular/core'; 
import { TranslateService } from '@ngx-translate/core'; 
import { NotificationsService } from 'src/app/services/notifications/notifications.service';
import { propiedades_globales as prop_glo } from '../../globals';

@Component({
  selector: 'app-button-list',
  templateUrl: './button-list.component.html',
  styleUrls: ['./button-list.component.css']
})
export class ButtonListComponent implements OnInit {
  
  constructor(  
        private notificationService:NotificationsService,
        public translate: TranslateService
  ) {
    translate.addLangs(prop_glo.info_globals.idiomas.config);
    translate.setDefaultLang(prop_glo.info_globals.idiomas.default);
  }

  public label_btn: any = prop_glo.label_btn;

  @Input() id: any;
  @Input() acciones_crud: any;

  ngOnInit(): void { } 

}
