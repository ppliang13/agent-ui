Vue.component('step-request', {
    props: ['step'],
    data() {
        return {
            envOptions: [],   // 环境列表
            appOptions: [],   // 当前环境的服务列表
            appMap: {}        // 服务名 -> URL 映射
        };
    },
    created() {
        this.loadEnvOptions();
    },
    methods: {
        // 加载环境列表
        loadEnvOptions() {
            axios.get('/ds/env')
                .then(res => {
                    this.envOptions = res.data || [];

                    // 如果已有环境，加载对应服务列表
                    if (this.step.env) {
                        this.loadAppOptions(this.step.env);
                    }
                })
                .catch(err => console.error(err));
        },
        // 根据环境加载服务列表
        loadAppOptions(env) {
            axios.get('/ds/listUrl', { params: { env } })
                .then(res => {
                    this.appMap = res.data || {};
                    this.appOptions = Object.keys(this.appMap);

                    // 如果已有选中的服务不存在于列表中，清空
                    if (this.step.app && !this.appOptions.includes(this.step.app)) {
                        Vue.set(this.step, 'app', '');
                    }
                })
                .catch(err => console.error(err));
        },
        // 环境改变事件
        onEnvChange(env) {
            Vue.set(this.step, 'env', env);
            Vue.set(this.step, 'app', '');
            this.appOptions = [];
            this.appMap = {};
            this.loadAppOptions(env);
        },
        // 格式化请求报文
        formatRequest() {
            if (!this.step.requestBody) return;
            axios.post('/ds/formatXMLOrJSON', this.step.requestBody)
                .then(res => {
                    this.step.requestBody = res.data;
                    this.$message.success('报文格式化完成');
                })
                .catch(err => {
                    console.error(err);
                    this.$message.error('格式化失败');
                });
        }
    },
    template: `
      <el-form label-width="120px">
        <el-form-item label="请求环境">
          <el-select v-model="step.env" placeholder="请选择环境" @change="onEnvChange">
            <el-option v-for="env in envOptions" :key="env" :label="env" :value="env"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="应用服务">
          <el-select v-model="step.app" placeholder="请选择应用服务">
            <el-option 
              v-for="app in appOptions" 
              :key="app" 
              :label="app" 
              :value="app">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="请求报文">
          <el-button type="primary" size="mini" @click="formatRequest" style="margin-bottom:5px">格式化报文</el-button>
          <textarea 
            class="code-editor" 
            v-model="step.requestBody" 
            placeholder="请输入请求报文，例如 JSON 或 XML">
          </textarea>
        </el-form-item>
      </el-form>
    `
});
