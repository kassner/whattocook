const path = require('path');

// @TODO WebpackManifestPlugin

module.exports = {
    entry: './src/main/js/App.tsx',
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/,
            },
        ],
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js'],
    },
    output: {
        path: path.resolve('./src/main/resources/static/js/'),
        publicPath: 'js/',
        filename: 'bundle.js', // @TODO use [contenthash]
    },
};
