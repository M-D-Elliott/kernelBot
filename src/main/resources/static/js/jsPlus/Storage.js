class Storage {

     static get(key){
        return localStorage.getItem(key);
     }

     static getOrI(key, i){
         const local = Storage.get(key);
         return local != null ? local : i;
     }

     static getParse(key){
        return JSON.parse(Storage.get(key));
     }
     static getParseOrI(key, i){
        const local = Storage.getParse(key);
        return local != null ? local : i;
     }

     static set(key, value){
        localStorage.setItem(key, value);
     }

     static setParse(key, value){
        Storage.set(key, JSON.stringify(value));
     }
}