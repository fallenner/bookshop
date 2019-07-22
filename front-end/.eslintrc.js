module.exports = {
    root: true,
    parserOptions: {
        sourceType: 'module',
        parser: 'babel-eslint',
        ecmaVersion: 2017
    },
    env: {
        browser: true
    },
    extends: [
        // add more generic rulesets here, such as:
        'eslint:recommended',
        'plugin:vue/essential'
    ],
    rules: {
        'no-console': 0,
        'no-undef': 0,
        'no-case-declarations': 0,
        'no-mixed-spaces-and-tabs': [0, 'smart-tabs'] //不允许混用tab和空格
    }
};
