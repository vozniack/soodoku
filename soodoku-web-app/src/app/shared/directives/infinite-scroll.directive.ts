import { Directive, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';

@Directive({
  selector: '[infiniteScroll]',
  standalone: true,
})
export class InfiniteScrollDirective implements OnInit {

  @Output() scrollReached: EventEmitter<void> = new EventEmitter<void>();

  @Input() threshold = 128;

  private window!: Window;
  isScrolling = false;

  constructor(private elementRef: ElementRef) {
  }

  ngOnInit(): void {
    this.window = window;
  }

  @HostListener('window:scroll', ['$event.target'])
  windowScrollEvent() {
    const heightOfWholePage = this.window.document.documentElement.scrollHeight;
    const heightOfElement = this.elementRef.nativeElement.scrollHeight;
    const currentScrolledY = this.window.scrollY;
    const innerHeight = this.window.innerHeight;

    const spaceOfElementAndPage = heightOfWholePage - heightOfElement;
    const scrollToBottom = heightOfElement - innerHeight - currentScrolledY + spaceOfElementAndPage;

    if (scrollToBottom < this.threshold && !this.isScrolling) {
      this.isScrolling = true;
      this.scrollReached.emit();

      setTimeout(() => this.isScrolling = false, 128);
    }
  }
}
