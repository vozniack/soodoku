import { ViewService } from './view.service';
import { View } from './view.const';

export abstract class ViewAwareComponent {

  View = View;

  protected constructor(private viewService: ViewService) {
  }

  protected setView(view: View) {
    this.viewService.setView(view);
  }
}
