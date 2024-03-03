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

describe('PasswordResets e2e test', () => {
  const passwordResetsPageUrl = '/password-resets';
  const passwordResetsPageUrlPattern = new RegExp('/password-resets(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const passwordResetsSample = { email: 'Lurline_Keebler59@hotmail.com', token: 'partial why dutiful' };

  let passwordResets;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/password-resets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/password-resets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/password-resets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (passwordResets) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/password-resets/${passwordResets.id}`,
      }).then(() => {
        passwordResets = undefined;
      });
    }
  });

  it('PasswordResets menu should load PasswordResets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('password-resets');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PasswordResets').should('exist');
    cy.url().should('match', passwordResetsPageUrlPattern);
  });

  describe('PasswordResets page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(passwordResetsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PasswordResets page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/password-resets/new$'));
        cy.getEntityCreateUpdateHeading('PasswordResets');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', passwordResetsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/password-resets',
          body: passwordResetsSample,
        }).then(({ body }) => {
          passwordResets = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/password-resets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/password-resets?page=0&size=20>; rel="last",<http://localhost/api/password-resets?page=0&size=20>; rel="first"',
              },
              body: [passwordResets],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(passwordResetsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PasswordResets page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('passwordResets');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', passwordResetsPageUrlPattern);
      });

      it('edit button click should load edit PasswordResets page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PasswordResets');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', passwordResetsPageUrlPattern);
      });

      it('edit button click should load edit PasswordResets page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PasswordResets');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', passwordResetsPageUrlPattern);
      });

      it('last delete button click should delete instance of PasswordResets', () => {
        cy.intercept('GET', '/api/password-resets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('passwordResets').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', passwordResetsPageUrlPattern);

        passwordResets = undefined;
      });
    });
  });

  describe('new PasswordResets page', () => {
    beforeEach(() => {
      cy.visit(`${passwordResetsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PasswordResets');
    });

    it('should create an instance of PasswordResets', () => {
      cy.get(`[data-cy="email"]`).type('Marina_Reichel32@yahoo.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Marina_Reichel32@yahoo.com');

      cy.get(`[data-cy="token"]`).type('deliberately about supposing');
      cy.get(`[data-cy="token"]`).should('have.value', 'deliberately about supposing');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T16:46');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T16:46');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        passwordResets = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', passwordResetsPageUrlPattern);
    });
  });
});
