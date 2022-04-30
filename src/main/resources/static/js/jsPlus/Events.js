class Events{

    static elMousePosEvent(event){
        return elMousePos(event.target);
    }

    static elMousePos(el){
          const rect = el.getBoundingClientRect();

          const x = event.clientX - rect.left;
          const y = event.clientY - rect.top;

          return { 'x': x, 'y': y};
    }

}