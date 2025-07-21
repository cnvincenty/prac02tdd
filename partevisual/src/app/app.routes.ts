import { Routes } from '@angular/router';
import { Index } from './principal/index';
import { GrupoproductoComponente } from './grupoproducto/presentacion/grupoproducto-componente';

export const routes: Routes = [
    {
        path: "",
        component: Index
    },
    {
        path: 'grupoproducto',
        loadComponent: () => import('./grupoproducto/presentacion/grupoproducto-componente').then(m => m.GrupoproductoComponente),
    },
];
