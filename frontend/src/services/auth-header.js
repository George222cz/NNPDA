export default function authHeader(jsonHeader = false) {
    const user = JSON.parse(localStorage.getItem('user'));

    if (user && user.accessToken) {
        if(jsonHeader){
            return {'Content-Type': 'application/json', Authorization: 'Bearer ' + user.accessToken}
        }
        return { Authorization: 'Bearer ' + user.accessToken };
    } else {
        if(jsonHeader){
            return {'Content-Type': 'application/json'}
        }
        return {};
    }
}