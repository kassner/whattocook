const path = require('path');
const autoprefixer = require('autoprefixer');

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
            {
                test: /\.(scss)$/,
                use: [
                    {loader: 'style-loader'},
                    {loader: 'css-loader'},
                    {
                        loader: 'postcss-loader',
                        options: {
                            postcssOptions: {
                                plugins: [autoprefixer]
                            }
                        }
                    },
                    {loader: 'sass-loader'}
                ]
            }
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
