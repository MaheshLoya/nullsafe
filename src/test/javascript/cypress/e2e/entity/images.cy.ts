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

describe('Images e2e test', () => {
  const imagesPageUrl = '/images';
  const imagesPageUrlPattern = new RegExp('/images(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const imagesSample = { tableName: 'oof', tableId: 22661, imageType: true, image: 'furthermore consequently needle' };

  let images;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/images+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/images').as('postEntityRequest');
    cy.intercept('DELETE', '/api/images/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (images) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/images/${images.id}`,
      }).then(() => {
        images = undefined;
      });
    }
  });

  it('Images menu should load Images page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('images');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Images').should('exist');
    cy.url().should('match', imagesPageUrlPattern);
  });

  describe('Images page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(imagesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Images page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/images/new$'));
        cy.getEntityCreateUpdateHeading('Images');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/images',
          body: imagesSample,
        }).then(({ body }) => {
          images = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/images+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/images?page=0&size=20>; rel="last",<http://localhost/api/images?page=0&size=20>; rel="first"',
              },
              body: [images],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(imagesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Images page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('images');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagesPageUrlPattern);
      });

      it('edit button click should load edit Images page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Images');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagesPageUrlPattern);
      });

      it('edit button click should load edit Images page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Images');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagesPageUrlPattern);
      });

      it('last delete button click should delete instance of Images', () => {
        cy.intercept('GET', '/api/images/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('images').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', imagesPageUrlPattern);

        images = undefined;
      });
    });
  });

  describe('new Images page', () => {
    beforeEach(() => {
      cy.visit(`${imagesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Images');
    });

    it('should create an instance of Images', () => {
      cy.get(`[data-cy="tableName"]`).type('depend excluding');
      cy.get(`[data-cy="tableName"]`).should('have.value', 'depend excluding');

      cy.get(`[data-cy="tableId"]`).type('3093');
      cy.get(`[data-cy="tableId"]`).should('have.value', '3093');

      cy.get(`[data-cy="imageType"]`).should('not.be.checked');
      cy.get(`[data-cy="imageType"]`).click();
      cy.get(`[data-cy="imageType"]`).should('be.checked');

      cy.get(`[data-cy="image"]`).type('ruffle graph yippee');
      cy.get(`[data-cy="image"]`).should('have.value', 'ruffle graph yippee');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T01:56');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T01:56');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T21:44');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T21:44');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        images = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', imagesPageUrlPattern);
    });
  });
});
