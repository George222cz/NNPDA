import authHeader from './auth-header';

class UserService {

    getDataAPI(controller,optionalURL = "", jsonResponse = true,jsonError = true){
        return fetch(process.env.REACT_APP_API_URL+controller+"/"+optionalURL, {
            method: 'GET',
            headers: authHeader()
        }).then(async response => {
                if (response.ok) {
                    return jsonResponse ? response.json() : response.text();
                }
                throw new Error(jsonError ? JSON.stringify(await response.json()) : (await response.text()));
            }
        );
    }

     postDataAPI(controller,body = undefined, optionalURL = "", jsonResponse = true, jsonError = true){
        return fetch(process.env.REACT_APP_API_URL+controller+"/"+optionalURL, {
            method: 'POST',
            headers: authHeader(true),
            body: JSON.stringify(body)
        }).then(async response => {
                if (response.ok) {
                    return jsonResponse ? response.json() : response.text();
                }
                throw new Error(jsonError ? JSON.stringify(await response.json()) : (await response.text()));
            }
        );
    }

    putDataAPI(controller,body, optionalURL = "", jsonResponse = true, jsonError = true){
        return fetch(process.env.REACT_APP_API_URL+controller+"/"+optionalURL, {
            method: 'PUT',
            headers: authHeader(true),
            body: JSON.stringify(body)
        }).then(async response => {
                if (response.ok) {
                    return jsonResponse ? response.json() : response.text();
                }
                throw new Error(jsonError ? JSON.stringify(await response.json()) : (await response.text()));
            }
        );
    }

    deleteDataAPI(controller,body = undefined, optionalURL = "", jsonResponse = true, jsonError = true){
        return fetch(process.env.REACT_APP_API_URL+controller+"/"+optionalURL, {
            method: 'DELETE',
            headers: authHeader(true),
            body: JSON.stringify(body)
        }).then(async response => {
                if (response.ok) {
                    return jsonResponse ? response.json() : response.text();
                }
                throw new Error(jsonError ? JSON.stringify(await response.json()) : (await response.text()));
            }
        );
    }

    getUser() {
        const user = JSON.parse(localStorage.getItem('user'));

        if (user && user.accessToken) {
            return user;
        } else {
            return "";
        }
    }

}
export default new UserService();