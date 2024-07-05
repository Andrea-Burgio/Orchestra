import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ClienteCorsoService } from '../service/cliente-corso.service';
import { IClienteCorso } from '../cliente-corso.model';
import { ClienteCorsoFormService } from './cliente-corso-form.service';

import { ClienteCorsoUpdateComponent } from './cliente-corso-update.component';

describe('ClienteCorso Management Update Component', () => {
  let comp: ClienteCorsoUpdateComponent;
  let fixture: ComponentFixture<ClienteCorsoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clienteCorsoFormService: ClienteCorsoFormService;
  let clienteCorsoService: ClienteCorsoService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ClienteCorsoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ClienteCorsoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClienteCorsoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clienteCorsoFormService = TestBed.inject(ClienteCorsoFormService);
    clienteCorsoService = TestBed.inject(ClienteCorsoService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const clienteCorso: IClienteCorso = { id: 456 };
      const cliente: ICliente = { id: 9995 };
      clienteCorso.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 1930 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clienteCorso });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const clienteCorso: IClienteCorso = { id: 456 };
      const cliente: ICliente = { id: 12912 };
      clienteCorso.cliente = cliente;

      activatedRoute.data = of({ clienteCorso });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.clienteCorso).toEqual(clienteCorso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClienteCorso>>();
      const clienteCorso = { id: 123 };
      jest.spyOn(clienteCorsoFormService, 'getClienteCorso').mockReturnValue(clienteCorso);
      jest.spyOn(clienteCorsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clienteCorso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clienteCorso }));
      saveSubject.complete();

      // THEN
      expect(clienteCorsoFormService.getClienteCorso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clienteCorsoService.update).toHaveBeenCalledWith(expect.objectContaining(clienteCorso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClienteCorso>>();
      const clienteCorso = { id: 123 };
      jest.spyOn(clienteCorsoFormService, 'getClienteCorso').mockReturnValue({ id: null });
      jest.spyOn(clienteCorsoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clienteCorso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clienteCorso }));
      saveSubject.complete();

      // THEN
      expect(clienteCorsoFormService.getClienteCorso).toHaveBeenCalled();
      expect(clienteCorsoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClienteCorso>>();
      const clienteCorso = { id: 123 };
      jest.spyOn(clienteCorsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clienteCorso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clienteCorsoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCliente', () => {
      it('Should forward to clienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
