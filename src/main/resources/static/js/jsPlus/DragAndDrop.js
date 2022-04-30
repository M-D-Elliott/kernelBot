class DragAndDrop{
         text.onmousedown = function(event) { //  start the process
                // get ready to move: make an absolute and top z-index
                text.style.position = 'absolute';
                text.style.zIndex = 1000;
                // move it from any existing parents directly to the body
                // to position it relative to the body
                document.body.append(text);
                // and put this absolutely positioned text under the pointer
                moveAt(event.pageX, event.pageY);

    static moveAt(pageX, pageY) {
      text.style.left = pageX - text.offsetWidth / 2 + 'px';
      text.style.top = pageY - text.offsetHeight / 2 + 'px';
    }
    static onMouseMove(event) {
      moveAt(event.pageX, event.pageY);
    }

                document.addEventListener('mousemove', onMouseMove);

                text.onmouseup = function() {
                  document.removeEventListener('mousemove', onMouseMove);
                  text.onmouseup = null;
                };
              };

    static prepare(){

    }
}