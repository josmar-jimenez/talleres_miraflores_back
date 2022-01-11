import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { MenuAutorization } from 'src/app/model/menu/menu-autorization';

const TOKEN_KEY = 'token';
const USERNAME_KEY = 'userName';
const AUTORIZATION_KEY = 'authorization';
const helper = new JwtHelperService();

@Injectable({
  providedIn: 'root'
})


export class AuthService {
  constructor( ) { } 

  public isAuthenticated(): boolean {
    const token = this.getToken();
    return !helper.isTokenExpired(token);
  }

  public logOut(): void {
    window.sessionStorage.clear();
    this.reloadPage();
  }

  public setToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): any {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public setUserName(token: string): void {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.setItem(USERNAME_KEY, token);
  }
  public getUserName(): string | null {
    return sessionStorage.getItem(USERNAME_KEY);
  }

  public setAutorization(modulosDelUsuario: string): void {
    window.sessionStorage.removeItem(AUTORIZATION_KEY);
    window.sessionStorage.setItem(AUTORIZATION_KEY, modulosDelUsuario);
  }
  public getAutorization(): string | null {
    return sessionStorage.getItem(AUTORIZATION_KEY);
  }

  public loadModuleMenu(): MenuAutorization[] {  
    let modulosUsuario: MenuAutorization[] = [];  

    let modulos: any = this.getAutorization();
    modulos =  JSON.parse((atob(modulos))); 
    let modulosAutorizados: string[] = [];

    modulos.forEach((module: any) => {  

      let agregarModulo:boolean = false;
      let existe = modulosAutorizados.indexOf(module.operative_name) != -1;

      if ( !existe ) {  
        modulosAutorizados.push(module.operative_name);  
        agregarModulo = true;                
      }
      
      if ( agregarModulo ){
        let acciones: string[] = [];  
        let mod = modulosAutorizados[modulosAutorizados.length - 1];
        
        modulos.forEach((accion: any) => {
          mod == accion.operative_name ?
          acciones.push(accion.action_name) :""; 
        });   

        modulosUsuario.push(this.createClassMenu(mod, acciones));

      } 

    });  
    
    return modulosUsuario;
  }

  public createClassMenu(mod: any, acciones:string[]): MenuAutorization{   
    let objMenu = new MenuAutorization(mod,this.getSymbolClass(mod), acciones);   
    return objMenu;
  }

  public getSymbolClass(modulo: string): string { 
    let symbol: string;

    switch (modulo) {
      case "user": {
        symbol = "nav-icon fas fa-users";
        break;
      }
      case "store": {
        symbol = "nav-icon fas fa-store";
        break;
      }
      case "product": {
        symbol = "nav-icon fas fa-luggage-cart";
        break;
      }
      case "stock": {
        symbol = "nav-icon fas fa-boxes";
        break;
      }
      case "provider": {
        symbol = "nav-icon far fa-building";
        break;
      }
      case "inventory": {
        symbol = "nav-icon fas fa-list-alt";
        break;
      }
      case "sale": {
        symbol = "nav-icon fas fa-cash-register";
        break;
      }
      case "tag": {
        symbol = "nav-icon fas fa-tags";
        break;
      }
      default: {
        symbol = "nav-icon far fa-frown-open";
        break;
      }
    }

    return symbol;
  }


  public reloadPage(): void {
    window.location.reload();
  }
}
