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

describe('AssignRole e2e test', () => {
  const assignRolePageUrl = '/assign-role';
  const assignRolePageUrlPattern = new RegExp('/assign-role(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assignRoleSample = {};

  let assignRole;
  let users;
  let role;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {
        walletAmount: 18343.17,
        email: 'Noe_Lindgren93@yahoo.com',
        phone: '868-477-7736 x5222',
        emailVerifiedAt: '2024-03-02T19:47:36.119Z',
        password: 'thunderhead',
        rememberToken: 'modulo gee',
        createdAt: '2024-03-02T09:23:13.428Z',
        updatedAt: '2024-03-02T20:21:55.963Z',
        name: 'where',
        fcm: 'infamous favour',
        subscriptionAmount: 19047,
      },
    }).then(({ body }) => {
      users = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/roles',
      body: { title: 'unknown flirt aha', createdAt: '2024-03-03T06:04:43.921Z', updatedAt: '2024-03-03T00:03:38.806Z', deleted: true },
    }).then(({ body }) => {
      role = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/assign-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/assign-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/assign-roles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [users],
    });

    cy.intercept('GET', '/api/roles', {
      statusCode: 200,
      body: [role],
    });
  });

  afterEach(() => {
    if (assignRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assign-roles/${assignRole.id}`,
      }).then(() => {
        assignRole = undefined;
      });
    }
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
    if (role) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/roles/${role.id}`,
      }).then(() => {
        role = undefined;
      });
    }
  });

  it('AssignRoles menu should load AssignRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('assign-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssignRole').should('exist');
    cy.url().should('match', assignRolePageUrlPattern);
  });

  describe('AssignRole page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assignRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssignRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/assign-role/new$'));
        cy.getEntityCreateUpdateHeading('AssignRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assignRolePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/assign-roles',
          body: {
            ...assignRoleSample,
            user: users,
            role: role,
          },
        }).then(({ body }) => {
          assignRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/assign-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/assign-roles?page=0&size=20>; rel="last",<http://localhost/api/assign-roles?page=0&size=20>; rel="first"',
              },
              body: [assignRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assignRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssignRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assignRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assignRolePageUrlPattern);
      });

      it('edit button click should load edit AssignRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssignRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assignRolePageUrlPattern);
      });

      it('edit button click should load edit AssignRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssignRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assignRolePageUrlPattern);
      });

      it('last delete button click should delete instance of AssignRole', () => {
        cy.intercept('GET', '/api/assign-roles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('assignRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', assignRolePageUrlPattern);

        assignRole = undefined;
      });
    });
  });

  describe('new AssignRole page', () => {
    beforeEach(() => {
      cy.visit(`${assignRolePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssignRole');
    });

    it('should create an instance of AssignRole', () => {
      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T02:10');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T02:10');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T22:43');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T22:43');

      cy.get(`[data-cy="user"]`).select(1);
      cy.get(`[data-cy="role"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        assignRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', assignRolePageUrlPattern);
    });
  });
});
