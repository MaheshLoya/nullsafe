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

describe('Cat e2e test', () => {
  const catPageUrl = '/cat';
  const catPageUrlPattern = new RegExp('/cat(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const catSample = { title: 'worst', isActive: true };

  let cat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cats').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cats/${cat.id}`,
      }).then(() => {
        cat = undefined;
      });
    }
  });

  it('Cats menu should load Cats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cat').should('exist');
    cy.url().should('match', catPageUrlPattern);
  });

  describe('Cat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(catPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cat/new$'));
        cy.getEntityCreateUpdateHeading('Cat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cats',
          body: catSample,
        }).then(({ body }) => {
          cat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cats?page=0&size=20>; rel="last",<http://localhost/api/cats?page=0&size=20>; rel="first"',
              },
              body: [cat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(catPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catPageUrlPattern);
      });

      it('edit button click should load edit Cat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catPageUrlPattern);
      });

      it('edit button click should load edit Cat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catPageUrlPattern);
      });

      it('last delete button click should delete instance of Cat', () => {
        cy.intercept('GET', '/api/cats/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catPageUrlPattern);

        cat = undefined;
      });
    });
  });

  describe('new Cat page', () => {
    beforeEach(() => {
      cy.visit(`${catPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cat');
    });

    it('should create an instance of Cat', () => {
      cy.get(`[data-cy="title"]`).type('form');
      cy.get(`[data-cy="title"]`).should('have.value', 'form');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T23:15');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T23:15');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T19:43');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T19:43');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        cat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', catPageUrlPattern);
    });
  });
});
