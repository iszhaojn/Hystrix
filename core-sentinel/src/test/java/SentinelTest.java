import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author zhaojn
 * @date 10:47 2019/6/5
 */
public class SentinelTest extends TestCase {


	private static void initFlowRules(){
		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule = new FlowRule();

		DegradeRule degradeRule = new DegradeRule();

		SystemRule systemRule = new SystemRule();
		systemRule.setResource("");


		rule.setResource("HelloWorld");
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		// Set limit QPS to 20.
		rule.setCount(20);
		rules.add(rule);
		FlowRuleManager.loadRules(rules);
	}


	public void test01(){


		FuseUtils.execute("", new FuseUtils.Process() {
			@Override
			public void success() {

			}

			@Override
			public void fallback() {

			}
		});

	}


	public static class FuseUtils {

		public static void execute(String group,Process process){
			try (Entry entry = SphU.entry("resourceName")) {
				process.success();
			} catch (BlockException ex) {
				process.fallback();
			}


		}


		public interface Process {

			void success();

			void fallback();

		}


	}



}


