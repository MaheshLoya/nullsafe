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

describe('UserHoliday e2e test', () => {
  const userHolidayPageUrl = '/user-holiday';
  const userHolidayPageUrlPattern = new RegExp('/user-holiday(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userHolidaySample = { userId: 22914, date: '2024-03-02' };

  let userHoliday;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-holidays+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-holidays').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-holidays/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userHoliday) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-holidays/${userHoliday.id}`,
      }).then(() => {
        userHoliday = undefined;
      });
    }
  });

  it('UserHolidays menu should load UserHolidays page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-holiday');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserHoliday').should('exist');
    cy.url().should('match', userHolidayPageUrlPattern);
  });

  describe('UserHoliday page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userHolidayPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserHoliday page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-holiday/new$'));
        cy.getEntityCreateUpdateHeading('UserHoliday');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userHolidayPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-holidays',
          body: userHolidaySample,
        }).then(({ body }) => {
          userHoliday = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-holidays+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-holidays?page=0&size=20>; rel="last",<http://localhost/api/user-holidays?page=0&size=20>; rel="first"',
              },
              body: [userHoliday],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userHolidayPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserHoliday page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userHoliday');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userHolidayPageUrlPattern);
      });

      it('edit button click should load edit UserHoliday page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserHoliday');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userHolidayPageUrlPattern);
      });

      it('edit button click should load edit UserHoliday page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserHoliday');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userHolidayPageUrlPattern);
      });

      it('last delete button click should delete instance of UserHoliday', () => {
        cy.intercept('GET', '/api/user-holidays/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userHoliday').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userHolidayPageUrlPattern);

        userHoliday = undefined;
      });
    });
  });

  describe('new UserHoliday page', () => {
    beforeEach(() => {
      cy.visit(`${userHolidayPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserHoliday');
    });

    it('should create an instance of UserHoliday', () => {
      cy.get(`[data-cy="userId"]`).type('17654');
      cy.get(`[data-cy="userId"]`).should('have.value', '17654');

      cy.get(`[data-cy="date"]`).type('2024-03-03');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-03-03');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-03T05:03');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-03T05:03');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T21:16');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T21:16');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userHoliday = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userHolidayPageUrlPattern);
    });
  });
});
