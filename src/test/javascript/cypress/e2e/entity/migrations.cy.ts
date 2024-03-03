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

describe('Migrations e2e test', () => {
  const migrationsPageUrl = '/migrations';
  const migrationsPageUrlPattern = new RegExp('/migrations(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const migrationsSample = { migration: 'than mortal', batch: 8338 };

  let migrations;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/migrations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/migrations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/migrations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (migrations) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/migrations/${migrations.id}`,
      }).then(() => {
        migrations = undefined;
      });
    }
  });

  it('Migrations menu should load Migrations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('migrations');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Migrations').should('exist');
    cy.url().should('match', migrationsPageUrlPattern);
  });

  describe('Migrations page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(migrationsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Migrations page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/migrations/new$'));
        cy.getEntityCreateUpdateHeading('Migrations');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', migrationsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/migrations',
          body: migrationsSample,
        }).then(({ body }) => {
          migrations = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/migrations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/migrations?page=0&size=20>; rel="last",<http://localhost/api/migrations?page=0&size=20>; rel="first"',
              },
              body: [migrations],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(migrationsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Migrations page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('migrations');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', migrationsPageUrlPattern);
      });

      it('edit button click should load edit Migrations page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Migrations');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', migrationsPageUrlPattern);
      });

      it('edit button click should load edit Migrations page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Migrations');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', migrationsPageUrlPattern);
      });

      it('last delete button click should delete instance of Migrations', () => {
        cy.intercept('GET', '/api/migrations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('migrations').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', migrationsPageUrlPattern);

        migrations = undefined;
      });
    });
  });

  describe('new Migrations page', () => {
    beforeEach(() => {
      cy.visit(`${migrationsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Migrations');
    });

    it('should create an instance of Migrations', () => {
      cy.get(`[data-cy="migration"]`).type('down');
      cy.get(`[data-cy="migration"]`).should('have.value', 'down');

      cy.get(`[data-cy="batch"]`).type('25143');
      cy.get(`[data-cy="batch"]`).should('have.value', '25143');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        migrations = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', migrationsPageUrlPattern);
    });
  });
});
