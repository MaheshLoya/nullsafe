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

describe('Files e2e test', () => {
  const filesPageUrl = '/files';
  const filesPageUrlPattern = new RegExp('/files(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const filesSample = { name: 'glorify woot', fileUrl: 'blah', deleted: false, fileFor: 30737, fileForId: 30064, fileCat: false };

  let files;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/files+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/files').as('postEntityRequest');
    cy.intercept('DELETE', '/api/files/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (files) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/files/${files.id}`,
      }).then(() => {
        files = undefined;
      });
    }
  });

  it('Files menu should load Files page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('files');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Files').should('exist');
    cy.url().should('match', filesPageUrlPattern);
  });

  describe('Files page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(filesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Files page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/files/new$'));
        cy.getEntityCreateUpdateHeading('Files');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/files',
          body: filesSample,
        }).then(({ body }) => {
          files = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/files+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/files?page=0&size=20>; rel="last",<http://localhost/api/files?page=0&size=20>; rel="first"',
              },
              body: [files],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(filesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Files page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('files');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filesPageUrlPattern);
      });

      it('edit button click should load edit Files page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Files');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filesPageUrlPattern);
      });

      it('edit button click should load edit Files page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Files');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filesPageUrlPattern);
      });

      it('last delete button click should delete instance of Files', () => {
        cy.intercept('GET', '/api/files/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('files').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filesPageUrlPattern);

        files = undefined;
      });
    });
  });

  describe('new Files page', () => {
    beforeEach(() => {
      cy.visit(`${filesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Files');
    });

    it('should create an instance of Files', () => {
      cy.get(`[data-cy="name"]`).type('and contrast');
      cy.get(`[data-cy="name"]`).should('have.value', 'and contrast');

      cy.get(`[data-cy="fileUrl"]`).type('senator doubtfully wet');
      cy.get(`[data-cy="fileUrl"]`).should('have.value', 'senator doubtfully wet');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T03:56');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T03:56');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-03T01:34');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-03T01:34');

      cy.get(`[data-cy="deleted"]`).should('not.be.checked');
      cy.get(`[data-cy="deleted"]`).click();
      cy.get(`[data-cy="deleted"]`).should('be.checked');

      cy.get(`[data-cy="fileFor"]`).type('25001');
      cy.get(`[data-cy="fileFor"]`).should('have.value', '25001');

      cy.get(`[data-cy="fileForId"]`).type('6891');
      cy.get(`[data-cy="fileForId"]`).should('have.value', '6891');

      cy.get(`[data-cy="fileCat"]`).should('not.be.checked');
      cy.get(`[data-cy="fileCat"]`).click();
      cy.get(`[data-cy="fileCat"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        files = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', filesPageUrlPattern);
    });
  });
});
