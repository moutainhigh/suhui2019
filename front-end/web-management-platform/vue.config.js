const path = require('path')

function resolve(dir) {
    return path.join(__dirname, dir)
}

// vue.config.js
module.exports = {
    // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
    productionSourceMap: false,
    configureWebpack: {},

    chainWebpack: config => {
        config.resolve.alias
            .set('@$', resolve('src'))
            .set('@api', resolve('src/api'))
            .set('@assets', resolve('src/assets'))
            .set('@comp', resolve('src/components'))
            .set('@views', resolve('src/views'))
            .set('@layout', resolve('src/layout'))
            .set('@static', resolve('src/static'))
    },

    css: {
        loaderOptions: {
            less: {
                modifyVars: {},
                javascriptEnabled: true
            }
        }
    },

    devServer: {
        port: 3000,
        proxy: {
            '/': {
                target: 'http://localhost:3333', //请求本地 需要jeecg-boot后台项目
                ws: false,
                changeOrigin: true
            }
        }
    },

    lintOnSave: undefined
}