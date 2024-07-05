import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IInsegnante } from 'app/entities/insegnante/insegnante.model';
import { InsegnanteService } from 'app/entities/insegnante/service/insegnante.service';
import { InsegnanteCorsoService } from '../service/insegnante-corso.service';
import { IInsegnanteCorso } from '../insegnante-corso.model';
import { InsegnanteCorsoFormService } from './insegnante-corso-form.service';

import { InsegnanteCorsoUpdateComponent } from './insegnante-corso-update.component';

describe('InsegnanteCorso Management Update Component', () => {
  let comp: InsegnanteCorsoUpdateComponent;
  let fixture: ComponentFixture<InsegnanteCorsoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let insegnanteCorsoFormService: InsegnanteCorsoFormService;
  let insegnanteCorsoService: InsegnanteCorsoService;
  let insegnanteService: InsegnanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, InsegnanteCorsoUpdateComponent],
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
      .overrideTemplate(InsegnanteCorsoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InsegnanteCorsoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    insegnanteCorsoFormService = TestBed.inject(InsegnanteCorsoFormService);
    insegnanteCorsoService = TestBed.inject(InsegnanteCorsoService);
    insegnanteService = TestBed.inject(InsegnanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Insegnante query and add missing value', () => {
      const insegnanteCorso: IInsegnanteCorso = { id: 456 };
      const insegnante: IInsegnante = { id: 20801 };
      insegnanteCorso.insegnante = insegnante;

      const insegnanteCollection: IInsegnante[] = [{ id: 13025 }];
      jest.spyOn(insegnanteService, 'query').mockReturnValue(of(new HttpResponse({ body: insegnanteCollection })));
      const additionalInsegnantes = [insegnante];
      const expectedCollection: IInsegnante[] = [...additionalInsegnantes, ...insegnanteCollection];
      jest.spyOn(insegnanteService, 'addInsegnanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ insegnanteCorso });
      comp.ngOnInit();

      expect(insegnanteService.query).toHaveBeenCalled();
      expect(insegnanteService.addInsegnanteToCollectionIfMissing).toHaveBeenCalledWith(
        insegnanteCollection,
        ...additionalInsegnantes.map(expect.objectContaining),
      );
      expect(comp.insegnantesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const insegnanteCorso: IInsegnanteCorso = { id: 456 };
      const insegnante: IInsegnante = { id: 19656 };
      insegnanteCorso.insegnante = insegnante;

      activatedRoute.data = of({ insegnanteCorso });
      comp.ngOnInit();

      expect(comp.insegnantesSharedCollection).toContain(insegnante);
      expect(comp.insegnanteCorso).toEqual(insegnanteCorso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsegnanteCorso>>();
      const insegnanteCorso = { id: 123 };
      jest.spyOn(insegnanteCorsoFormService, 'getInsegnanteCorso').mockReturnValue(insegnanteCorso);
      jest.spyOn(insegnanteCorsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insegnanteCorso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insegnanteCorso }));
      saveSubject.complete();

      // THEN
      expect(insegnanteCorsoFormService.getInsegnanteCorso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(insegnanteCorsoService.update).toHaveBeenCalledWith(expect.objectContaining(insegnanteCorso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsegnanteCorso>>();
      const insegnanteCorso = { id: 123 };
      jest.spyOn(insegnanteCorsoFormService, 'getInsegnanteCorso').mockReturnValue({ id: null });
      jest.spyOn(insegnanteCorsoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insegnanteCorso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: insegnanteCorso }));
      saveSubject.complete();

      // THEN
      expect(insegnanteCorsoFormService.getInsegnanteCorso).toHaveBeenCalled();
      expect(insegnanteCorsoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInsegnanteCorso>>();
      const insegnanteCorso = { id: 123 };
      jest.spyOn(insegnanteCorsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ insegnanteCorso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(insegnanteCorsoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInsegnante', () => {
      it('Should forward to insegnanteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(insegnanteService, 'compareInsegnante');
        comp.compareInsegnante(entity, entity2);
        expect(insegnanteService.compareInsegnante).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
