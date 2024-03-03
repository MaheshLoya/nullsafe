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

describe('AllowPincode e2e test', () => {
  const allowPincodePageUrl = '/allow-pincode';
  const allowPincodePageUrlPattern = new RegExp('/allow-pincode(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const allowPincodeSample = { pinCode: 5849 };

  let allowPincode;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/allow-pincodes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/allow-pincodes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/allow-pincodes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (allowPincode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/allow-pincodes/${allowPincode.id}`,
      }).then(() => {
        allowPincode = undefined;
      });
    }
  });

  it('AllowPincodes menu should load AllowPincodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('allow-pincode');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AllowPincode').should('exist');
    cy.url().should('match', allowPincodePageUrlPattern);
  });

  describe('AllowPincode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(allowPincodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AllowPincode page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/allow-pincode/new$'));
        cy.getEntityCreateUpdateHeading('AllowPincode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', allowPincodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/allow-pincodes',
          body: allowPincodeSample,
        }).then(({ body }) => {
          allowPincode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/allow-pincodes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/allow-pincodes?page=0&size=20>; rel="last",<http://localhost/api/allow-pincodes?page=0&size=20>; rel="first"',
              },
              body: [allowPincode],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(allowPincodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AllowPincode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('allowPincode');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', allowPincodePageUrlPattern);
      });

      it('edit button click should load edit AllowPincode page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AllowPincode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', allowPincodePageUrlPattern);
      });

      it('edit button click should load edit AllowPincode page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AllowPincode');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', allowPincodePageUrlPattern);
      });

      it('last delete button click should delete instance of AllowPincode', () => {
        cy.intercept('GET', '/api/allow-pincodes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('allowPincode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', allowPincodePageUrlPattern);

        allowPincode = undefined;
      });
    });
  });

  describe('new AllowPincode page', () => {
    beforeEach(() => {
      cy.visit(`${allowPincodePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AllowPincode');
    });

    it('should create an instance of AllowPincode', () => {
      cy.get(`[data-cy="pinCode"]`).type('25033');
      cy.get(`[data-cy="pinCode"]`).should('have.value', '25033');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T10:33');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T10:33');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T16:09');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T16:09');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        allowPincode = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', allowPincodePageUrlPattern);
    });
  });
});
