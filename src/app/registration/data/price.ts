import { TicketType } from './models';

export const MAX_TICKETS = 5;
export const DISCOUNT_FROM_QUANTITY = 3;
export const DISCOUNT_RATE = 0.1;

export const UNIT_PRICES: Record<TicketType, number> = {
  standard: 49,
  vip: 99,
};

export interface PriceBreakdown {
  unitPrice: number;
  subtotal: number;
  discount: number;
  total: number;
}

export function calculatePrice(ticketType: TicketType, quantity: number): PriceBreakdown {
  const unitPrice = UNIT_PRICES[ticketType];
  if (!Number.isInteger(quantity) || quantity < 1) {
    return { unitPrice, subtotal: 0, discount: 0, total: 0 };
  }

  const subtotal = unitPrice * quantity;
  const discount = quantity >= DISCOUNT_FROM_QUANTITY ? roundCents(subtotal * DISCOUNT_RATE) : 0;
  return { unitPrice, subtotal, discount, total: roundCents(subtotal - discount) };
}

function roundCents(value: number): number {
  return Math.round(value * 100) / 100;
}
