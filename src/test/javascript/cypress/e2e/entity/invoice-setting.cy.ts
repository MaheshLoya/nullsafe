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

describe('InvoiceSetting e2e test', () => {
  const invoiceSettingPageUrl = '/invoice-setting';
  const invoiceSettingPageUrlPattern = new RegExp('/invoice-setting(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const invoiceSettingSample = { title: 'pfft glamorize rubbery', value: 'indeed' };

  let invoiceSetting;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/invoice-settings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoice-settings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoice-settings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (invoiceSetting) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-settings/${invoiceSetting.id}`,
      }).then(() => {
        invoiceSetting = undefined;
      });
    }
  });

  it('InvoiceSettings menu should load InvoiceSettings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice-setting');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InvoiceSetting').should('exist');
    cy.url().should('match', invoiceSettingPageUrlPattern);
  });

  describe('InvoiceSetting page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(invoiceSettingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InvoiceSetting page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/invoice-setting/new$'));
        cy.getEntityCreateUpdateHeading('InvoiceSetting');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', invoiceSettingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/invoice-settings',
          body: invoiceSettingSample,
        }).then(({ body }) => {
          invoiceSetting = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/invoice-settings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/invoice-settings?page=0&size=20>; rel="last",<http://localhost/api/invoice-settings?page=0&size=20>; rel="first"',
              },
              body: [invoiceSetting],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(invoiceSettingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InvoiceSetting page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('invoiceSetting');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', invoiceSettingPageUrlPattern);
      });

      it('edit button click should load edit InvoiceSetting page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceSetting');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', invoiceSettingPageUrlPattern);
      });

      it('edit button click should load edit InvoiceSetting page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceSetting');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', invoiceSettingPageUrlPattern);
      });

      it('last delete button click should delete instance of InvoiceSetting', () => {
        cy.intercept('GET', '/api/invoice-settings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('invoiceSetting').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', invoiceSettingPageUrlPattern);

        invoiceSetting = undefined;
      });
    });
  });

  describe('new InvoiceSetting page', () => {
    beforeEach(() => {
      cy.visit(`${invoiceSettingPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InvoiceSetting');
    });

    it('should create an instance of InvoiceSetting', () => {
      cy.get(`[data-cy="title"]`).type('catastrophe');
      cy.get(`[data-cy="title"]`).should('have.value', 'catastrophe');

      cy.get(`[data-cy="value"]`).type('justify');
      cy.get(`[data-cy="value"]`).should('have.value', 'justify');

      cy.get(`[data-cy="createdAt"]`).type('2024-03-02T16:31');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-03-02T16:31');

      cy.get(`[data-cy="updatedAt"]`).type('2024-03-02T12:24');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-03-02T12:24');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        invoiceSetting = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', invoiceSettingPageUrlPattern);
    });
  });
});
