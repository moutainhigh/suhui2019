package org.suhui.config;

public class AlipayConfig {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101000653805";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCOV9UD7qBTQGabVjsGvcN1z71aKerN1DAKXa45lbTbeW9vMTIlXzFPyDxOfmvTU2ymf+JpgmPqAOOeEmjuv/hDrqCuNWnh8mrbdvM6zbreG1i96EEgWlXJvo99kVezDW79b90SFuScnAgO+l2n6amS6VAWco8OCxt6d93a2qx4U2QzYpAp33jf7bfmSwgX+e839O5rXyLf/Ww7wqW/aAhHpFweT/XEx79BO78DSA86EmajBqfEgAbQ3gjEC9kWCTzFUSWz+2nCUlXviMqMQ1Hdr5CPf2K++hImKb9wSqGjbkwcA3/nIKDBIoVNxq8lLOgWwNogKR95QnhyFgLBUmu3AgMBAAECggEAMqtfX3v1RE1beRY0As1QI8LkU5EyTTyoMThxyXvaGnpayYkIobcGFbFgjkyAU4SR258QWKCasnTIkb9Z7QsW8/jYp8rJuy2RDjiEkr0jNOclKszfSgX+HcegyoEPZ+RzSvIkDslpbbo5QCOtbRVHUM2Ovb7f347ipDDBH6PR2y6/9u5VRusK5qeEgD6AJZT2HVooLaD6DGYiYoLlCMNX5yaf3JE61UpLvgJWVI3a3zUnWPJvE+IH56+KiQ4IsAJ0KhogS+Ce/bw7nl86yVJWjxrpUVvDdMyCbaM50+6g9scWt7HaK8VNMveMScYrBMFUn+vUTuQWvCIThiorbMmsEQKBgQD1cbrrJNUmb43nSIocPzzFmORk5oP3qfQnR5Q+IKgOwQIa2M8TTjKZoOeBqeR+bwaGsSxUfUUG9gHrrXN6P7DnSOIc4sE8spsN+JxjQrYA4TkrfIRqmCgowRu3yPavZTs/5gtMNi/6T1hc37wjev0dwC+onyc/uO4ZVaqgeemvOQKBgQCUdvz2BedaAMT/4a3pM30BCbwUXlwIo/o0hPvru8J1WFeJV9bQPdZfds35virOYCSBDlpzg8q11L4htmgRbhLZGIGTQi2UdeBfc7zBQ4EX3UrsnY1pi5b1scNcf6z/QMcQN/BCM8WAucoUDHAsUTLyUpyab283vr2RIutLjakCbwKBgQDmxkxtk3Amd3UBJZ9V9894Ks10YZlrYA0EcbTcxW++j5MfEwOAr2G8OSHzNH52soV8V1HLDw28AcrEgY22P/RhQpH3fu0Z2QUZGcxROARQx3MthCIs52u7Bfa90uK+/3u6FN5ChiCORdRbXrJS2ezr8Cvfa+VHcOXORFHuGTKzYQKBgCg6baTESMHsf3TOU+9ikjabyqjCQLHerMdaWWCwyao+O8ntUTmv58yKRpmrW7H7/ygxGXU/esf2U2aTJHPEnbrRlxqtzefyYM+8bqL3hZ1PyyN+NXkp58e/WS5RMjEf/lB4t9NYb+XHcegozqRThDiBBgoas8opFviiu5sHUYplAoGALwXdOj/jpY/zVQ6LCeewr3mcECgjx2fc6aQJuCEBFS19acBGL3d3q7V3QJgvV8O0dqXdGL2SqRZRed6+PoQqP1ryRNIFDs3/iV9Q+TZo7lgA6n61p+5SepKztv2qiTQpM7xrx+KZO6Cl54w4JPcmXBWwH6sYAzo99UsYtK/h7lk=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzp04pi06ld6lBUBjMLFLyQ2dnfNhSS2r0RsV4NR/G/UJf/nKfOZEnsMT1zz4r2JctXNIGl9v0eepAyMVCfCjvly3Sfa5ePGOJlMoEuhpyAaxZ7aJ+ROwsPP7Fv7sNQwQNkFUYygUmsuKDvzGVlvANZX3R7QHResrVEReGTD2I6FEAdmJgVbczULoqy7E4keTtLCFGam0l1y3etpudB4nvsJaLorOGxndn7+mC7M8dPRI2HrYFtVyC0g3xVPsYQPxHWMsMDwNMDdqi0TMZnqm72+yk4LgRviNLM0lDMHqZ8IVhxQzjOeFCK1qEV0Vtu2gXxE7Wld2WGKrJM6hXCivTwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:3333/jeecg-boot/api/pay/alipayNotifyNotice";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:3333/jeecg-boot/api/pay/alipayReturnNotice";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

}
