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

describe('UserNotification e2e test', () => {
  const userNotificationPageUrl = '/user-notification';
  const userNotificationPageUrlPattern = new RegExp('/user-notification(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userNotificationSample = { title: 'who outside', body: 'aboard' };

  let userNotification;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-notifications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-notifications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-notifications/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userNotification) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-notifications/${userNotification.id}`,
      }).then(() => {
        userNotification = undefined;
      });
    }
  });

  it('UserNotifications menu should load UserNotifications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-notification');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserNotification').should('exist');
    cy.url().should('match', userNotificationPageUrlPattern);
  });

  describe('UserNotification page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userNotificationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserNotification page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-notification/new$'));
        cy.getEntityCreateUpdateHeading('UserNotification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userNotificationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-notifications',
          body: userNotificationSample,
        }).then(({ body }) => {
          userNotification = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-notifications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-notifications?page=0&size=20>; rel="last",<http://localhost/api/user-notifications?page=0&size=20>; rel="first"',
              },
              body: [userNotification],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userNotificationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserNotification page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userNotification');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userNotificationPageUrlPattern);
      });

      it('edit button click should load edit UserNotification page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserNotification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userNotificationPageUrlPattern);
      });

      it('edit button click should load edit UserNotification page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserNotification');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userNotificationPageUrlPattern);
      });

      it('last delete button click should delete instance of UserNotification', () => {
        cy.intercept('GET', '/api/user-notifications/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userNotification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userNotificationPageUrlPattern);

        userNotification = undefined;
      });
    });
  });

  describe('new UserNotification page', () => {
    beforeEach(() => {
      cy.visit(`${userNotificationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserNotification');
    });

    it('should create an instance of UserNotification', () => {
      cy.get(`[data-cy="title"]`).type('afore cautiously valuable');
      cy.get(`[data-cy="title"]`).should('have.value', 'afore cautiously valuable');

      cy.get(`[data-cy="body"]`).type('scaly vigilant proper');
      cy.get(`[data-cy="body"]`).should('have.value', 'scaly vigilant proper');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T14:42');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T14:42');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T23:41');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T23:41');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userNotification = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userNotificationPageUrlPattern);
    });
  });
});
