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

describe('Users e2e test', () => {
  const usersPageUrl = '/users';
  const usersPageUrlPattern = new RegExp('/users(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const usersSample = { name: 'nobody fooey cobweb', subscriptionAmount: 31053 };

  let users;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (users) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${users.id}`,
      }).then(() => {
        users = undefined;
      });
    }
  });

  it('Users menu should load Users page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('users');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Users').should('exist');
    cy.url().should('match', usersPageUrlPattern);
  });

  describe('Users page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(usersPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Users page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/users/new$'));
        cy.getEntityCreateUpdateHeading('Users');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/users',
          body: usersSample,
        }).then(({ body }) => {
          users = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/users?page=0&size=20>; rel="last",<http://localhost/api/users?page=0&size=20>; rel="first"',
              },
              body: [users],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(usersPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Users page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('users');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);
      });

      it('edit button click should load edit Users page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Users');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);
      });

      it('edit button click should load edit Users page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Users');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);
      });

      it('last delete button click should delete instance of Users', () => {
        cy.intercept('GET', '/api/users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('users').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', usersPageUrlPattern);

        users = undefined;
      });
    });
  });

  describe('new Users page', () => {
    beforeEach(() => {
      cy.visit(`${usersPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Users');
    });

    it('should create an instance of Users', () => {
      cy.get(`[data-cy="walletAmount"]`).type('31604.75');
      cy.get(`[data-cy="walletAmount"]`).should('have.value', '31604.75');

      cy.get(`[data-cy="email"]`).type('Glenda67@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Glenda67@gmail.com');

      cy.get(`[data-cy="phone"]`).type('(860) 423-9750');
      cy.get(`[data-cy="phone"]`).should('have.value', '(860) 423-9750');

      cy.get(`[data-cy="emailVerifiedAt"]`).type('2024-03-02T07:49');
      cy.get(`[data-cy="emailVerifiedAt"]`).blur();
      cy.get(`[data-cy="emailVerifiedAt"]`).should('have.value', '2024-03-02T07:49');

      cy.get(`[data-cy="password"]`).type('before upliftingly but');
      cy.get(`[data-cy="password"]`).should('have.value', 'before upliftingly but');

      cy.get(`[data-cy="rememberToken"]`).type('zero boo whoever');
      cy.get(`[data-cy="rememberToken"]`).should('have.value', 'zero boo whoever');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T23:48');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T23:48');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-03T05:01');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-03T05:01');

      cy.get(`[data-cy="name"]`).type('elliptical certainly still');
      cy.get(`[data-cy="name"]`).should('have.value', 'elliptical certainly still');

      cy.get(`[data-cy="fcm"]`).type('reconfirm van');
      cy.get(`[data-cy="fcm"]`).should('have.value', 'reconfirm van');

      cy.get(`[data-cy="subscriptionAmount"]`).type('25733');
      cy.get(`[data-cy="subscriptionAmount"]`).should('have.value', '25733');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        users = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', usersPageUrlPattern);
    });
  });
});
