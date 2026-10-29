import { Component, input } from '@angular/core';
import { FieldTree } from '@angular/forms/signals';

@Component({
  selector: 'app-field-errors',
  template: `
    @if (field()().touched() && field()().invalid()) {
      <ul class="errors">
        @for (error of field()().errors(); track error) {
          <li>{{ error.message }}</li>
        }
      </ul>
    }
  `,
})
export class FieldErrors {
  readonly field = input.required<FieldTree<unknown>>();
}
