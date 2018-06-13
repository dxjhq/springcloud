package com.hhly.businesscomm.ice;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectPrx;
import Ice.Util;
import com.hhly.api.exception.ServiceException;
import com.hhly.utils.AssembleUtil;
import com.hhly.utils.LogUtil;
import com.hhly.utils.ValueUtil;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

public class IceUtils {

    /**
     * <pre>
     * 比如有如下 user.ice 文件
     * [["java:package:com.hhly.business"]]
     * module rpc {
     *   interface UserManager {
     *     string operate(string userInfo);
     *   };
     * };
     * 使用 slice2java user.ice 将会生成很多文件.
     *
     *
     * 对于服务端来说, 可以新建一个 UserService 并继承刚刚生成的 _UserManagerDisp, 将真正的逻辑写在 operate 方法中.
     * 而后服务器端使用下面的代码运行就会在当前机器开启一个 tcp 端口为 10000 的服务供客户端调用:
     *
     * Ice.Communicator ic = Ice.Util.initialize(args);
     * Ice.ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints("Simple", "tcp -h 127.0.0.1 -p 10000");
     * adapter.add(new UserService(), ic.stringToIdentity("UserXyz"));
     * adapter.activate();
     * ic.waitForShutdown();
     *
     *
     * 客户端只需要下面的类就可以调用了(依 ice 客户端版本而定, 3.6.2 只需要这几个就可以了)
     *     Callback_UserManager_login
     *     UserManagerPrx
     *     UserManagerPrxHelper
     *     UserManagerPrxHolder
     *
     * 当要调用服务器端时, 可以使用下面的代码:
     * Object result = getClient("配置文件", "ip", "端口", "tcp", 超时时间,
     *      "UserXyz", UserManager(|Prx|PrxHelper).class, "operate", 调用的方法参数);
     * if (result != null) {
     *     handle(result)...
     * }
     * </pre>
     *
     * @param cfgPath ice 的配置文件, 从 classpath 开始
     * @param ip 服务器 ip
     * @param port 服务器端口
     * @param connType 服务器类型, ssl, tcp, udp 等
     * @param timeOut 连接服务器超时时间, 单位: 毫秒. 1000 表示 1 秒
     * @param key 服务端定义好的接口的名字， 上面的示例中: UserXyz
     * @param clazz 服务端定义好的接口类, 上面的示例中: (UserManager、UserManagerPrx、UserManagerPrxHelper).class 都可以
     * @param method 接口提供服务的方法名, 上面的示例中: operate
     * @param params 调用服务方法的参数, 上面的示例中, 传给 UserManager#operate 方法中的参数 userInfo 的值
     */
    public static Object getClient(String cfgPath, String ip, String port, String connType, int timeOut,
                                   String key, Class<?> clazz, String method, Object... params) {
        ValueUtil.assertNil(key, "调用方未传入接口名");
        ValueUtil.assertNil(connType, "调用方未传入服务器类型");
        ValueUtil.assertNil(ip, "调用方未传入服务器 ip");
        ValueUtil.assertNil(port, "调用方未传入服务器端口");

        String proxy = key + ":" + connType.toLowerCase() + " -h " + ip + " -p " + port;
        String conn = String.format("  连接配置: %s\n  调用接口: %s#%s\n  传入参数: %s",
                proxy, clazz.getName(), method, AssembleUtil.toStr(params));
        Communicator communicator = null;
        try {
            InitializationData initData = new InitializationData();
            initData.properties = Ice.Util.createProperties();
            if (ValueUtil.isNotBlank(cfgPath)) {
                initData.properties.load(cfgPath);
            }
            communicator = Util.initialize(initData);

            ObjectPrx objPrx = communicator.stringToProxy(proxy).ice_twoway().ice_timeout(timeOut);
            // 静态调用 InterfacePrxHelper.checkedCast(ObjectPrx), 将返回 InterfacePrx 对象. 这一步将会建立连接
            Object obj = MethodUtils.invokeStaticMethod(forClass(clazz), "checkedCast", objPrx);
            if (obj != null) {
                // 在 InterfacePrx 对象上调用实际的方法, 这正是调用服务器的关键步骤, 这个调用通常是传入 json 返回也是 json.
                Object result = MethodUtils.invokeMethod(obj, method, params);
                if (LogUtil.ROOT_LOG.isInfoEnabled()) {
                    LogUtil.ROOT_LOG.info(conn + "\n  返回结果: {}", (result == null ? ValueUtil.EMPTY : result.toString()));
                }
                return result;
            }
            return null;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            if (LogUtil.ROOT_LOG.isErrorEnabled())
                LogUtil.ROOT_LOG.error("连接(\n" + conn + "\n)时异常", e);
            throw new ServiceException("连接服务器异常");
        } catch (Exception e) {
            if (LogUtil.ROOT_LOG.isErrorEnabled())
                LogUtil.ROOT_LOG.error("调用(\n" + conn + "\n)时异常", e);
            throw new ServiceException("调用服务器异常");
        } finally {
            if (communicator != null) {
                communicator.destroy();
            }
        }
    }

    /** 传入的 class 可以是 Class, 也可以是 ClassPrx, 还可以是 ClassPreHelper */
    private static Class<?> forClass(Class<?> clazz) {
        if (clazz == null) return null;

        String clazzName = clazz.getName();
        if (clazzName.endsWith("PrxHelper")) {
            return clazz;
        }

        try {
            return Class.forName(clazzName + (clazzName.endsWith("Prx") ? "Helper" : "PrxHelper"));
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            if (LogUtil.ROOT_LOG.isErrorEnabled())
                LogUtil.ROOT_LOG.error("class exception");
            throw new ServiceException("传入的类参数有错");
        }
    }
}
