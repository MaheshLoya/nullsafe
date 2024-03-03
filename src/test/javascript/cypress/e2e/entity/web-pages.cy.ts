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

describe('WebPages e2e test', () => {
  const webPagesPageUrl = '/web-pages';
  const webPagesPageUrlPattern = new RegExp('/web-pages(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const webPagesSample = { pageId: 3330, title: 'coaster meringue soggy', body: 'er' };

  let webPages;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/web-pages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/web-pages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/web-pages/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (webPages) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/web-pages/${webPages.id}`,
      }).then(() => {
        webPages = undefined;
      });
    }
  });

  it('WebPages menu should load WebPages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('web-pages');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WebPages').should('exist');
    cy.url().should('match', webPagesPageUrlPattern);
  });

  describe('WebPages page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(webPagesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WebPages page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/web-pages/new$'));
        cy.getEntityCreateUpdateHeading('WebPages');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webPagesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/web-pages',
          body: webPagesSample,
        }).then(({ body }) => {
          webPages = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/web-pages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/web-pages?page=0&size=20>; rel="last",<http://localhost/api/web-pages?page=0&size=20>; rel="first"',
              },
              body: [webPages],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(webPagesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WebPages page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('webPages');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webPagesPageUrlPattern);
      });

      it('edit button click should load edit WebPages page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WebPages');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webPagesPageUrlPattern);
      });

      it('edit button click should load edit WebPages page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WebPages');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webPagesPageUrlPattern);
      });

      it('last delete button click should delete instance of WebPages', () => {
        cy.intercept('GET', '/api/web-pages/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('webPages').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webPagesPageUrlPattern);

        webPages = undefined;
      });
    });
  });

  describe('new WebPages page', () => {
    beforeEach(() => {
      cy.visit(`${webPagesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WebPages');
    });

    it('should create an instance of WebPages', () => {
      cy.get(`[data-cy="pageId"]`).type('30104');
      cy.get(`[data-cy="pageId"]`).should('have.value', '30104');

      cy.get(`[data-cy="title"]`).type('unto');
      cy.get(`[data-cy="title"]`).should('have.value', 'unto');

      cy.get(`[data-cy="body"]`).type('spicy');
      cy.get(`[data-cy="body"]`).should('have.value', 'spicy');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T08:12');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T08:12');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T14:53');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T14:53');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        webPages = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', webPagesPageUrlPattern);
    });
  });
});
