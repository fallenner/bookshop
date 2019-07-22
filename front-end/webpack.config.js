const webpack = require('webpack');
const VueLoaderPlugin = require('vue-loader/lib/plugin');
const HtmlWebPackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

const entries = [
    {
        chunkName: 'bundle',
        entryFile: './src/index.js',
        template: './src/template/index.html',
        filename: 'index.html'
    }
];

const tempEntries = {};
entries.forEach(item => (tempEntries[item.chunkName] = item.entryFile));

module.exports = (env, argv) => ({
    entry: tempEntries,
    module: {
        rules: [
            {
                enforce: 'pre',
                test: /\.(js|vue)$/,
                exclude: /(node_modules|static)/,
                use: [
                    {
                        loader: 'eslint-loader',
                        options: {
                            emitError: true
                        }
                    }
                ]
            },
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {
                test: /\.css$/,
                use: [
                    argv.mode !== 'production'
                        ? 'vue-style-loader'
                        : {
                              loader: MiniCssExtractPlugin.loader
                          },
                    'css-loader'
                ]
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: 'babel-loader'
            },
            {
                test: /\.(png|jpg|gif|svg|eot|ttf|woff|woff2|ico)$/,
                use: [
                    {
                        loader: 'url-loader',
                        options: {
                            limit: 8192, //在代码中引用静态资源中的图片，用require才会打包。
                            name: 'assets/[name].[ext]' //超出8k的图片将单独打包到指定目录并按照一定规则重命名
                            // publicPath: argv.mode === 'development' ? '/' : ''
                        }
                    }
                ]
            }
        ]
    },
    //  优化打包体积加快首页渲染速度
    optimization: {
        splitChunks: {
            minSize: 30000,
            cacheGroups: {
                'third-vendor': {
                    chunks: 'all',
                    // test: /[\\/]node_modules[\\/](moment|lodash)/,
                    test: /[\\/]node_modules[\\/](moment|lodash|js-cookie|es6-promise)[\\/]/,
                    name: 'common/third-vendor',
                    priority: -10
                },
                // 单独打包vue插件
                'vue-vendor': {
                    chunks: 'initial', // 'initial', 'async', 'all',
                    test: /[\\/]node_modules[\\/](vue|vuex|vue-router|axios)[\\/]/, // <- window | mac -> /node_modules/vue/
                    name: 'common/vue-vendor',
                    priority: -8
                }
            }
        }
    },
    plugins: [
        //减少moment打包的体积
        new webpack.ContextReplacementPlugin(/moment[/\\]locale$/, /zh-cn/),
        // 请确保引入这个插件！
        new VueLoaderPlugin(),
        ...entries.map(
            item =>
                new HtmlWebPackPlugin({
                    chunks: [
                        'common/third-vendor',
                        'common/vue-vendor',
                        item.chunkName
                    ],
                    title: '网上书店',
                    hash: true,
                    // favicon: './src/common/images/favicon.ico',
                    ...item
                })
        ),
        new MiniCssExtractPlugin()
    ],
    resolve: {
        extensions: ['.js', '.vue', '.json'],
        alias: {
            vue$: 'vue/dist/vue.esm.js'
        }
    },
    devServer: {
        host: '0.0.0.0',
        overlay: true,
        open: true,
        port: '3001'
    },
    devtool: argv.mode === 'development' ? 'source-map' : ''
});
