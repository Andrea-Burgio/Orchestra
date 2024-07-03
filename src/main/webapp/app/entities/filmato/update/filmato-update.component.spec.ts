import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { FilmatoService } from '../service/filmato.service';
import { IFilmato } from '../filmato.model';
import { FilmatoFormService } from './filmato-form.service';

import { FilmatoUpdateComponent } from './filmato-update.component';

describe('Filmato Management Update Component', () => {
  let comp: FilmatoUpdateComponent;
  let fixture: ComponentFixture<FilmatoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let filmatoFormService: FilmatoFormService;
  let filmatoService: FilmatoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, FilmatoUpdateComponent],
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
      .overrideTemplate(FilmatoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FilmatoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    filmatoFormService = TestBed.inject(FilmatoFormService);
    filmatoService = TestBed.inject(FilmatoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const filmato: IFilmato = { id: 456 };

      activatedRoute.data = of({ filmato });
      comp.ngOnInit();

      expect(comp.filmato).toEqual(filmato);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFilmato>>();
      const filmato = { id: 123 };
      jest.spyOn(filmatoFormService, 'getFilmato').mockReturnValue(filmato);
      jest.spyOn(filmatoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filmato });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filmato }));
      saveSubject.complete();

      // THEN
      expect(filmatoFormService.getFilmato).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(filmatoService.update).toHaveBeenCalledWith(expect.objectContaining(filmato));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFilmato>>();
      const filmato = { id: 123 };
      jest.spyOn(filmatoFormService, 'getFilmato').mockReturnValue({ id: null });
      jest.spyOn(filmatoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filmato: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filmato }));
      saveSubject.complete();

      // THEN
      expect(filmatoFormService.getFilmato).toHaveBeenCalled();
      expect(filmatoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFilmato>>();
      const filmato = { id: 123 };
      jest.spyOn(filmatoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filmato });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(filmatoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
