class APIs{
    static ping(url){
        fetch(url, {
            type: 'HEAD',
            headers: {"Access-Control-Allow-Origin": url}
        })
        .then(() => {
            console.log("ok");
        }).catch(() =>{
            console.log("error");
        });
    }
}