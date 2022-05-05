class kBUtils {
    static send(commandString, receiverFunc) {
        fetch('/', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: commandString
        })
            .then(resp => resp.json())
            .then(json => {
                receiverFunc(json);
            });
    }
}