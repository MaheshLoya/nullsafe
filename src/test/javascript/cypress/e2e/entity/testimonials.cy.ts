import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Testimonials e2e test', () => {
  const testimonialsPageUrl = '/testimonials';
  const testimonialsPageUrlPattern = new RegExp('/testimonials(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const testimonialsSample = { title: 'which livid rigid', subTitle: 'speedily', rating: 21590, description: 'outgun' };

  let testimonials;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/testimonials+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/testimonials').as('postEntityRequest');
    cy.intercept('DELETE', '/api/testimonials/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (testimonials) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/testimonials/${testimonials.id}`,
      }).then(() => {
        testimonials = undefined;
      });
    }
  });

  it('Testimonials menu should load Testimonials page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('testimonials');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Testimonials').should('exist');
    cy.url().should('match', testimonialsPageUrlPattern);
  });

  describe('Testimonials page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(testimonialsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Testimonials page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/testimonials/new$'));
        cy.getEntityCreateUpdateHeading('Testimonials');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testimonialsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/testimonials',
          body: testimonialsSample,
        }).then(({ body }) => {
          testimonials = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/testimonials+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/testimonials?page=0&size=20>; rel="last",<http://localhost/api/testimonials?page=0&size=20>; rel="first"',
              },
              body: [testimonials],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(testimonialsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Testimonials page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('testimonials');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testimonialsPageUrlPattern);
      });

      it('edit button click should load edit Testimonials page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Testimonials');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testimonialsPageUrlPattern);
      });

      it('edit button click should load edit Testimonials page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Testimonials');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testimonialsPageUrlPattern);
      });

      it('last delete button click should delete instance of Testimonials', () => {
        cy.intercept('GET', '/api/testimonials/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('testimonials').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', testimonialsPageUrlPattern);

        testimonials = undefined;
      });
    });
  });

  describe('new Testimonials page', () => {
    beforeEach(() => {
      cy.visit(`${testimonialsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Testimonials');
    });

    it('should create an instance of Testimonials', () => {
      cy.get(`[data-cy="title"]`).type('intimate');
      cy.get(`[data-cy="title"]`).should('have.value', 'intimate');

      cy.get(`[data-cy="subTitle"]`).type('upon eek');
      cy.get(`[data-cy="subTitle"]`).should('have.value', 'upon eek');

      cy.get(`[data-cy="rating"]`).type('8264');
      cy.get(`[data-cy="rating"]`).should('have.value', '8264');

      cy.get(`[data-cy="description"]`).type('excitedly scientific');
      cy.get(`[data-cy="description"]`).should('have.value', 'excitedly scientific');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T14:57');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T14:57');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T13:03');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T13:03');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        testimonials = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', testimonialsPageUrlPattern);
    });
  });
});
