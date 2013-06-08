single = load('exp2_result1_2.data');
node4 = load('exp2_result2_2.data');
node6 = load('exp2_result3_2.data');
node8 = load('exp2_result4_2.data');
size=load('exp2_result0_2.data');

plot(size,single ./ node4,'k-+',size,single ./ node6,'r-o',size,single ./ node8,'b-*');
legend('4 nodes','6 nodes','8 nodes');
xlabel('Testing Data Lines');
ylabel('Speadup');
title('Speedup of Voting based Combination on Hadoop Cluster');