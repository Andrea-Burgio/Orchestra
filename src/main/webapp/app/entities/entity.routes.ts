import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'corso',
    data: { pageTitle: 'Corsos' },
    loadChildren: () => import('./corso/corso.routes'),
  },
  {
    path: 'insegnante',
    data: { pageTitle: 'Insegnantes' },
    loadChildren: () => import('./insegnante/insegnante.routes'),
  },
  {
    path: 'cliente',
    data: { pageTitle: 'Clientes' },
    loadChildren: () => import('./cliente/cliente.routes'),
  },
  {
    path: 'concerto',
    data: { pageTitle: 'Concertos' },
    loadChildren: () => import('./concerto/concerto.routes'),
  },
  {
    path: 'foto',
    data: { pageTitle: 'Fotos' },
    loadChildren: () => import('./foto/foto.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
