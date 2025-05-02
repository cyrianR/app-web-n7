import { createStore, createLogger } from 'vuex'
import { auth } from './modules/auth';

const debug = process.env.NODE_ENV !== 'production'

const store = createStore({
    modules: {
        // add imported modules here
        auth
    },
    strict: debug,
    plugins: debug ? [createLogger()] : []
})

export default store