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

describe('UserAddress e2e test', () => {
  const userAddressPageUrl = '/user-address';
  const userAddressPageUrlPattern = new RegExp('/user-address(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userAddressSample = {
    userId: 25130,
    name: 'snare ouch',
    sPhone: 'overlook',
    area: 'for er streetcar',
    landmark: 'finally um huzzah',
    city: 'Kassandrafort',
    pincode: 8387,
  };

  let userAddress;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-addresses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-addresses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-addresses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userAddress) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-addresses/${userAddress.id}`,
      }).then(() => {
        userAddress = undefined;
      });
    }
  });

  it('UserAddresses menu should load UserAddresses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-address');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserAddress').should('exist');
    cy.url().should('match', userAddressPageUrlPattern);
  });

  describe('UserAddress page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userAddressPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserAddress page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-address/new$'));
        cy.getEntityCreateUpdateHeading('UserAddress');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-addresses',
          body: userAddressSample,
        }).then(({ body }) => {
          userAddress = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-addresses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-addresses?page=0&size=20>; rel="last",<http://localhost/api/user-addresses?page=0&size=20>; rel="first"',
              },
              body: [userAddress],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userAddressPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserAddress page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userAddress');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);
      });

      it('edit button click should load edit UserAddress page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAddress');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);
      });

      it('edit button click should load edit UserAddress page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAddress');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);
      });

      it('last delete button click should delete instance of UserAddress', () => {
        cy.intercept('GET', '/api/user-addresses/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userAddress').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userAddressPageUrlPattern);

        userAddress = undefined;
      });
    });
  });

  describe('new UserAddress page', () => {
    beforeEach(() => {
      cy.visit(`${userAddressPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserAddress');
    });

    it('should create an instance of UserAddress', () => {
      cy.get(`[data-cy="userId"]`).type('7259');
      cy.get(`[data-cy="userId"]`).should('have.value', '7259');

      cy.get(`[data-cy="name"]`).type('except yuck');
      cy.get(`[data-cy="name"]`).should('have.value', 'except yuck');

      cy.get(`[data-cy="sPhone"]`).type('vastly');
      cy.get(`[data-cy="sPhone"]`).should('have.value', 'vastly');

      cy.get(`[data-cy="flatNo"]`).type('toward vastly');
      cy.get(`[data-cy="flatNo"]`).should('have.value', 'toward vastly');

      cy.get(`[data-cy="apartmentName"]`).type('unlike');
      cy.get(`[data-cy="apartmentName"]`).should('have.value', 'unlike');

      cy.get(`[data-cy="area"]`).type('nicely until');
      cy.get(`[data-cy="area"]`).should('have.value', 'nicely until');

      cy.get(`[data-cy="landmark"]`).type('shrilly');
      cy.get(`[data-cy="landmark"]`).should('have.value', 'shrilly');

      cy.get(`[data-cy="city"]`).type('Stephenbury');
      cy.get(`[data-cy="city"]`).should('have.value', 'Stephenbury');

      cy.get(`[data-cy="pincode"]`).type('849');
      cy.get(`[data-cy="pincode"]`).should('have.value', '849');

      cy.get(`[data-cy="lat"]`).type('8294.71');
      cy.get(`[data-cy="lat"]`).should('have.value', '8294.71');

      cy.get(`[data-cy="lng"]`).type('18822.1');
      cy.get(`[data-cy="lng"]`).should('have.value', '18822.1');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T12:59');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T12:59');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T13:36');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T13:36');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userAddress = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userAddressPageUrlPattern);
    });
  });
});
