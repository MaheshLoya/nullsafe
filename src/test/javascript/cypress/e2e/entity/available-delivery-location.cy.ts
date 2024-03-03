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

describe('AvailableDeliveryLocation e2e test', () => {
  const availableDeliveryLocationPageUrl = '/available-delivery-location';
  const availableDeliveryLocationPageUrlPattern = new RegExp('/available-delivery-location(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const availableDeliveryLocationSample = { title: 'better quicker fine' };

  let availableDeliveryLocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/available-delivery-locations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/available-delivery-locations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/available-delivery-locations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (availableDeliveryLocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/available-delivery-locations/${availableDeliveryLocation.id}`,
      }).then(() => {
        availableDeliveryLocation = undefined;
      });
    }
  });

  it('AvailableDeliveryLocations menu should load AvailableDeliveryLocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('available-delivery-location');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AvailableDeliveryLocation').should('exist');
    cy.url().should('match', availableDeliveryLocationPageUrlPattern);
  });

  describe('AvailableDeliveryLocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(availableDeliveryLocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AvailableDeliveryLocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/available-delivery-location/new$'));
        cy.getEntityCreateUpdateHeading('AvailableDeliveryLocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', availableDeliveryLocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/available-delivery-locations',
          body: availableDeliveryLocationSample,
        }).then(({ body }) => {
          availableDeliveryLocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/available-delivery-locations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/available-delivery-locations?page=0&size=20>; rel="last",<http://localhost/api/available-delivery-locations?page=0&size=20>; rel="first"',
              },
              body: [availableDeliveryLocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(availableDeliveryLocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AvailableDeliveryLocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('availableDeliveryLocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', availableDeliveryLocationPageUrlPattern);
      });

      it('edit button click should load edit AvailableDeliveryLocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AvailableDeliveryLocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', availableDeliveryLocationPageUrlPattern);
      });

      it('edit button click should load edit AvailableDeliveryLocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AvailableDeliveryLocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', availableDeliveryLocationPageUrlPattern);
      });

      it('last delete button click should delete instance of AvailableDeliveryLocation', () => {
        cy.intercept('GET', '/api/available-delivery-locations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('availableDeliveryLocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', availableDeliveryLocationPageUrlPattern);

        availableDeliveryLocation = undefined;
      });
    });
  });

  describe('new AvailableDeliveryLocation page', () => {
    beforeEach(() => {
      cy.visit(`${availableDeliveryLocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AvailableDeliveryLocation');
    });

    it('should create an instance of AvailableDeliveryLocation', () => {
      cy.get(`[data-cy="title"]`).type('knight whoever');
      cy.get(`[data-cy="title"]`).should('have.value', 'knight whoever');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T16:53');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T16:53');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-03T05:21');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-03T05:21');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        availableDeliveryLocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', availableDeliveryLocationPageUrlPattern);
    });
  });
});
