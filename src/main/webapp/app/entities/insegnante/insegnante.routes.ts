import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InsegnanteComponent } from './list/insegnante.component';
import { InsegnanteDetailComponent } from './detail/insegnante-detail.component';
import { InsegnanteUpdateComponent } from './update/insegnante-update.component';
import InsegnanteResolve from './route/insegnante-routing-resolve.service';

const insegnanteRoute: Routes = [
  {
    path: '',
    component: InsegnanteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InsegnanteDetailComponent,
    resolve: {
      insegnante: InsegnanteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InsegnanteUpdateComponent,
    resolve: {
      insegnante: InsegnanteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InsegnanteUpdateComponent,
    resolve: {
      insegnante: InsegnanteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default insegnanteRoute;
