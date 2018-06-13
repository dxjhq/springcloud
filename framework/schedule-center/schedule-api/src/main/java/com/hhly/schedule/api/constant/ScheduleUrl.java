package com.hhly.schedule.api.constant;

/**
 * @author pengchao
 * @create 2017-09-19
 * @desc
 */
public class ScheduleUrl {
    public static String buildURL(String url) {
        return SERVICE_HOSTNAME + url;
    }

    /** service name **/
    public static final String SERVICE_NAME = "SCHEDULE";

    public static final String SERVICE_HOSTNAME = "http://SCHEDULE";

    public static final String SCHEDULE_TASK_PAGE = "/schedule/task/page";
    public static final String SCHEDULE_TASK_GET = "/schedule/task/get";
    public static final String SCHEDULE_TASK_DELETE = "/schedule/task/delete";
    public static final String SCHEDULE_TASK_UPDATE = "/schedule/task/update";
    public static final String SCHEDULE_TASK_REGISTER = "/schedule/task/register";

    public static final String SCHEDULE_TASK_ENABLE = "/schedule/task/enable";
    public static final String SCHEDULE_TASK_END = "/schedule/task/end";
    public static final String SCHEDULE_TASK_DISABLE = "/schedule/task/disable";
    public static final String SCHEDULE_TASK_EXECUTE = "/schedule/task/execute";

    public static final String SCHEDULE_DEMO_EXECUTE = "/schedule/demo/execute";
    public static final String SCHEDULE_DEMO_EXECUTE1 = "/schedule/demo/execute1";
    public static final String JOB_DEMO_EXECUTE = "/job/demo/execute";
    public static final String JOB_DEMO_EXECUTE1 = "/job/demo/execute1";
    public static final String TASK_DEMO_EXECUTE = "/task/demo/execute";

    public static final String EXECUTE_LOG_PAGE = "/schedule/tasklog/page";
    public static final String EXECUTE_LOG_GET = "/schedule/tasklog/get";
    public static final String EXECUTE_LOG_DELETE = "/schedule/tasklog/delete";
    public static final String EXECUTE_LOG_UPDATE = "/schedule/tasklog/update";
    public static final String EXECUTE_LOG_ADD = "/schedule/tasklog/add";
}
