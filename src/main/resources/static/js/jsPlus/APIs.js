class APIs{
    static ping(url, successCb, errorCb, timeout){
        let headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }

        fetch(url, {
            mode: 'no-cors',
            credentials: 'include',
            method: 'HEAD',
            headers: {},
            timeout: timeout
        })
        .then(resp => {
            if(successCb != null) successCb(resp);
        })
        .catch(error => {
            if(errorCb != null) errorCb(error);
        });
    }
}