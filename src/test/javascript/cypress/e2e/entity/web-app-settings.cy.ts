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

describe('WebAppSettings e2e test', () => {
  const webAppSettingsPageUrl = '/web-app-settings';
  const webAppSettingsPageUrlPattern = new RegExp('/web-app-settings(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const webAppSettingsSample = { title: 'flatboat clumsy', value: 'wetly' };

  let webAppSettings;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/web-app-settings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/web-app-settings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/web-app-settings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (webAppSettings) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/web-app-settings/${webAppSettings.id}`,
      }).then(() => {
        webAppSettings = undefined;
      });
    }
  });

  it('WebAppSettings menu should load WebAppSettings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('web-app-settings');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WebAppSettings').should('exist');
    cy.url().should('match', webAppSettingsPageUrlPattern);
  });

  describe('WebAppSettings page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(webAppSettingsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WebAppSettings page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/web-app-settings/new$'));
        cy.getEntityCreateUpdateHeading('WebAppSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webAppSettingsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/web-app-settings',
          body: webAppSettingsSample,
        }).then(({ body }) => {
          webAppSettings = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/web-app-settings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/web-app-settings?page=0&size=20>; rel="last",<http://localhost/api/web-app-settings?page=0&size=20>; rel="first"',
              },
              body: [webAppSettings],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(webAppSettingsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WebAppSettings page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('webAppSettings');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webAppSettingsPageUrlPattern);
      });

      it('edit button click should load edit WebAppSettings page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WebAppSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webAppSettingsPageUrlPattern);
      });

      it('edit button click should load edit WebAppSettings page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WebAppSettings');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webAppSettingsPageUrlPattern);
      });

      it('last delete button click should delete instance of WebAppSettings', () => {
        cy.intercept('GET', '/api/web-app-settings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('webAppSettings').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', webAppSettingsPageUrlPattern);

        webAppSettings = undefined;
      });
    });
  });

  describe('new WebAppSettings page', () => {
    beforeEach(() => {
      cy.visit(`${webAppSettingsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WebAppSettings');
    });

    it('should create an instance of WebAppSettings', () => {
      cy.get(`[data-cy="title"]`).type('unto hence');
      cy.get(`[data-cy="title"]`).should('have.value', 'unto hence');

      cy.get(`[data-cy="value"]`).type('which');
      cy.get(`[data-cy="value"]`).should('have.value', 'which');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T13:31');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T13:31');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T09:53');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T09:53');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        webAppSettings = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', webAppSettingsPageUrlPattern);
    });
  });
});
