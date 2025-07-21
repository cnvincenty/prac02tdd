import { Routes } from '@angular/router';
import { Index } from './principal/index';

export const routes: Routes = [
    {
        path: "",
        component: Index
    },
    {
        path: 'grupoproducto',
        loadComponent: () => import('./grupoproducto/presentacion/grupoproducto-componente').then(m => m.GrupoproductoComponente),
    },
    {
        path: 'grupocliente',
        loadComponent: () => import('./grupocliente/presentacion/grupocliente-componente').then(m => m.GrupoclienteComponente),
    },
];
