class APIs {
    static ping(url, successCb, errorCb, timeout) {
        let headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }

        fetch(url, {
            mode: 'no-cors',
            credentials: 'include',
            method: 'HEAD',
            headers: headers,
            timeout: timeout
        })
            .then(() => {
                if (successCb != null) successCb();
            })
            .catch(() => {
                if (errorCb != null) errorCb();
            });
    }
}