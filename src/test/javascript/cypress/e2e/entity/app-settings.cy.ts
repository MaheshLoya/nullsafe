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

describe('AppSettings e2e test', () => {
  const appSettingsPageUrl = '/app-settings';
  const appSettingsPageUrlPattern = new RegExp('/app-settings(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const appSettingsSample = { settingId: 32544, title: 'eyeliner', value: 'numb geez crown' };

  let appSettings;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/app-settings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/app-settings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/app-settings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (appSettings) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/app-settings/${appSettings.id}`,
      }).then(() => {
        appSettings = undefined;
      });
    }
  });

  it('AppSettings menu should load AppSettings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('app-settings');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AppSettings').should('exist');
    cy.url().should('match', appSettingsPageUrlPattern);
  });

  describe('AppSettings page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(appSettingsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AppSettings page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/app-settings/new$'));
        cy.getEntityCreateUpdateHeading('AppSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', appSettingsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/app-settings',
          body: appSettingsSample,
        }).then(({ body }) => {
          appSettings = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/app-settings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/app-settings?page=0&size=20>; rel="last",<http://localhost/api/app-settings?page=0&size=20>; rel="first"',
              },
              body: [appSettings],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(appSettingsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AppSettings page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('appSettings');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', appSettingsPageUrlPattern);
      });

      it('edit button click should load edit AppSettings page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AppSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', appSettingsPageUrlPattern);
      });

      it('edit button click should load edit AppSettings page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AppSettings');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', appSettingsPageUrlPattern);
      });

      it('last delete button click should delete instance of AppSettings', () => {
        cy.intercept('GET', '/api/app-settings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('appSettings').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', appSettingsPageUrlPattern);

        appSettings = undefined;
      });
    });
  });

  describe('new AppSettings page', () => {
    beforeEach(() => {
      cy.visit(`${appSettingsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AppSettings');
    });

    it('should create an instance of AppSettings', () => {
      cy.get(`[data-cy="settingId"]`).type('28910');
      cy.get(`[data-cy="settingId"]`).should('have.value', '28910');

      cy.get(`[data-cy="title"]`).type('because bah');
      cy.get(`[data-cy="title"]`).should('have.value', 'because bah');

      cy.get(`[data-cy="value"]`).type('infect supposing');
      cy.get(`[data-cy="value"]`).should('have.value', 'infect supposing');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T14:15');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T14:15');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T14:34');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T14:34');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        appSettings = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', appSettingsPageUrlPattern);
    });
  });
});
