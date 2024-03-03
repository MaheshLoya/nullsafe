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

describe('PersonalAccessTokens e2e test', () => {
  const personalAccessTokensPageUrl = '/personal-access-tokens';
  const personalAccessTokensPageUrlPattern = new RegExp('/personal-access-tokens(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const personalAccessTokensSample = {
    tokenableType: 'overwork',
    tokenableId: 18289,
    name: 'exemplary lest',
    token: 'an boutique appreciation',
  };

  let personalAccessTokens;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/personal-access-tokens+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/personal-access-tokens').as('postEntityRequest');
    cy.intercept('DELETE', '/api/personal-access-tokens/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (personalAccessTokens) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/personal-access-tokens/${personalAccessTokens.id}`,
      }).then(() => {
        personalAccessTokens = undefined;
      });
    }
  });

  it('PersonalAccessTokens menu should load PersonalAccessTokens page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('personal-access-tokens');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PersonalAccessTokens').should('exist');
    cy.url().should('match', personalAccessTokensPageUrlPattern);
  });

  describe('PersonalAccessTokens page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(personalAccessTokensPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PersonalAccessTokens page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/personal-access-tokens/new$'));
        cy.getEntityCreateUpdateHeading('PersonalAccessTokens');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalAccessTokensPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/personal-access-tokens',
          body: personalAccessTokensSample,
        }).then(({ body }) => {
          personalAccessTokens = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/personal-access-tokens+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/personal-access-tokens?page=0&size=20>; rel="last",<http://localhost/api/personal-access-tokens?page=0&size=20>; rel="first"',
              },
              body: [personalAccessTokens],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(personalAccessTokensPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PersonalAccessTokens page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('personalAccessTokens');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalAccessTokensPageUrlPattern);
      });

      it('edit button click should load edit PersonalAccessTokens page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonalAccessTokens');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalAccessTokensPageUrlPattern);
      });

      it('edit button click should load edit PersonalAccessTokens page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonalAccessTokens');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalAccessTokensPageUrlPattern);
      });

      it('last delete button click should delete instance of PersonalAccessTokens', () => {
        cy.intercept('GET', '/api/personal-access-tokens/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('personalAccessTokens').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', personalAccessTokensPageUrlPattern);

        personalAccessTokens = undefined;
      });
    });
  });

  describe('new PersonalAccessTokens page', () => {
    beforeEach(() => {
      cy.visit(`${personalAccessTokensPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PersonalAccessTokens');
    });

    it('should create an instance of PersonalAccessTokens', () => {
      cy.get(`[data-cy="tokenableType"]`).type('inside');
      cy.get(`[data-cy="tokenableType"]`).should('have.value', 'inside');

      cy.get(`[data-cy="tokenableId"]`).type('27442');
      cy.get(`[data-cy="tokenableId"]`).should('have.value', '27442');

      cy.get(`[data-cy="name"]`).type('helmet surprised letter');
      cy.get(`[data-cy="name"]`).should('have.value', 'helmet surprised letter');

      cy.get(`[data-cy="token"]`).type('bomb');
      cy.get(`[data-cy="token"]`).should('have.value', 'bomb');

      cy.get(`[data-cy="abilities"]`).type('muddy');
      cy.get(`[data-cy="abilities"]`).should('have.value', 'muddy');

      cy.get(`[data-cy="lastUsedAt"]`).type('2024-03-02T22:54');
      cy.get(`[data-cy="lastUsedAt"]`).blur();
      cy.get(`[data-cy="lastUsedAt"]`).should('have.value', '2024-03-02T22:54');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T01:42');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T01:42');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-03T06:01');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-03T06:01');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        personalAccessTokens = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', personalAccessTokensPageUrlPattern);
    });
  });
});
