import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterPageObject } from './pages/register.page';
import { TestSetupUtil } from './utils/test-setup.util';
import { HttpTestUtil } from './utils/http-test.util';
import { RouterTestUtil } from './utils/router.util';
import { page } from 'vitest/browser';

type TicketType = 'standard' | 'vip';

type RegistrationData = {
  name: string;
  email: string;
  ticketType: TicketType;
  quantity: number;
};

describe('Registration Form', () => {
  let registerPage: RegisterPageObject;
  let fixture: ComponentFixture<unknown>;

  beforeEach(async () => {
    TestSetupUtil.setupTestEnvironment();

    fixture = await RouterTestUtil.navigateToRegister();
    registerPage = new RegisterPageObject(fixture);
  });

  it('fills form and validates price VIP without discount', async () => {
    const registrationData: RegistrationData = {
      name: 'Fato',
      email: 'fato@mail.com',
      ticketType: 'vip',
      quantity: 2,
    };

    registerPage.setName(registrationData.name);
    await registerPage.setEmail(registrationData.email);
    registerPage.selectTicketType(registrationData.ticketType);
    registerPage.setQuantity(registrationData.quantity);
    registerPage.acceptTerms();

    fixture.detectChanges();
    const price = calcPrice(registrationData.ticketType, registrationData.quantity);
    expect(registerPage.getTotalPrice()).toBe(price);

    HttpTestUtil.verify();
  });

  it('fills form and validates price VIP with discount', async () => {
    const registrationData: RegistrationData = {
      name: 'Fato',
      email: 'fato@mail.com',
      ticketType: 'vip',
      quantity: 3,
    };

    registerPage.setName(registrationData.name);
    await registerPage.setEmail(registrationData.email);
    registerPage.selectTicketType(registrationData.ticketType);
    registerPage.setQuantity(registrationData.quantity);
    registerPage.acceptTerms();

    fixture.detectChanges();
    const price = calcPrice(registrationData.ticketType, registrationData.quantity);
    expect(registerPage.getTotalPrice()).toBe(price);

    HttpTestUtil.verify();
  });

  it('fills form and validates price standard without discount', async () => {
    const registrationData: RegistrationData = {
      name: 'Fato',
      email: 'fato@mail.com',
      ticketType: 'standard',
      quantity: 2,
    };

    registerPage.setName(registrationData.name);
    await registerPage.setEmail(registrationData.email);
    registerPage.selectTicketType(registrationData.ticketType);
    registerPage.setQuantity(registrationData.quantity);
    registerPage.acceptTerms();

    fixture.detectChanges();
    const price = calcPrice(registrationData.ticketType, registrationData.quantity);
    expect(registerPage.getTotalPrice()).toBe(price);

    HttpTestUtil.verify();
  });

  it('fills form and validates price standard with discount', async () => {
    const registrationData: RegistrationData = {
      name: 'Fato',
      email: 'fato@mail.com',
      ticketType: 'standard',
      quantity: 3,
    };

    registerPage.setName(registrationData.name);
    await registerPage.setEmail(registrationData.email);
    registerPage.selectTicketType(registrationData.ticketType);
    registerPage.setQuantity(registrationData.quantity);
    registerPage.acceptTerms();

    fixture.detectChanges();
    const price = calcPrice(registrationData.ticketType, registrationData.quantity);
    expect(registerPage.getTotalPrice()).toBe(price);

    HttpTestUtil.verify();
  });
});

function calcPrice(ticketType: TicketType, quantity: number) {
  const pricePerTicket = ticketType === 'standard' ? 49 : 99;
  const overallPrice = quantity * pricePerTicket;

  return quantity > 2 ? overallPrice - overallPrice * 0.1 : overallPrice;
}
