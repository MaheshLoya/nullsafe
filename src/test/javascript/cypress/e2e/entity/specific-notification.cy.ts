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

describe('SpecificNotification e2e test', () => {
  const specificNotificationPageUrl = '/specific-notification';
  const specificNotificationPageUrlPattern = new RegExp('/specific-notification(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const specificNotificationSample = { title: 'but harsh', body: 'under frantically meh' };

  let specificNotification;
  let users;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {
        walletAmount: 13483.82,
        email: 'Queen_Cruickshank@yahoo.com',
        phone: '1-756-271-2966 x70405',
        emailVerifiedAt: '2024-03-03T02:50:47.017Z',
        password: 'alongside next nor',
        rememberToken: 'contrary regarding literate',
        createdAt: '2024-03-02T22:05:39.391Z',
        updatedAt: '2024-03-03T01:03:56.395Z',
        name: 'finally than once',
        fcm: 'off now',
        subscriptionAmount: 17404,
      },
    }).then(({ body }) => {
      users = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/specific-notifications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/specific-notifications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/specific-notifications/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [users],
    });
  });

  afterEach(() => {
    if (specificNotification) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/specific-notifications/${specificNotification.id}`,
      }).then(() => {
        specificNotification = undefined;
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
  });

  it('SpecificNotifications menu should load SpecificNotifications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('specific-notification');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SpecificNotification').should('exist');
    cy.url().should('match', specificNotificationPageUrlPattern);
  });

  describe('SpecificNotification page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(specificNotificationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SpecificNotification page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/specific-notification/new$'));
        cy.getEntityCreateUpdateHeading('SpecificNotification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specificNotificationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/specific-notifications',
          body: {
            ...specificNotificationSample,
            user: users,
          },
        }).then(({ body }) => {
          specificNotification = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/specific-notifications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/specific-notifications?page=0&size=20>; rel="last",<http://localhost/api/specific-notifications?page=0&size=20>; rel="first"',
              },
              body: [specificNotification],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(specificNotificationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SpecificNotification page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('specificNotification');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specificNotificationPageUrlPattern);
      });

      it('edit button click should load edit SpecificNotification page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SpecificNotification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specificNotificationPageUrlPattern);
      });

      it('edit button click should load edit SpecificNotification page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SpecificNotification');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specificNotificationPageUrlPattern);
      });

      it('last delete button click should delete instance of SpecificNotification', () => {
        cy.intercept('GET', '/api/specific-notifications/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('specificNotification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specificNotificationPageUrlPattern);

        specificNotification = undefined;
      });
    });
  });

  describe('new SpecificNotification page', () => {
    beforeEach(() => {
      cy.visit(`${specificNotificationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SpecificNotification');
    });

    it('should create an instance of SpecificNotification', () => {
      cy.get(`[data-cy="title"]`).type('quash rumble likely');
      cy.get(`[data-cy="title"]`).should('have.value', 'quash rumble likely');

      cy.get(`[data-cy="body"]`).type('truly dilution zowie');
      cy.get(`[data-cy="body"]`).should('have.value', 'truly dilution zowie');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T11:21');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T11:21');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-03T07:21');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-03T07:21');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        specificNotification = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', specificNotificationPageUrlPattern);
    });
  });
});
