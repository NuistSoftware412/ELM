single = load('exp2_result1.data');
node4 = load('exp2_result2.data');
node6 = load('exp2_result3.data');
node8 = load('exp2_result4.data');
size=load('exp2_result0.data');

plot(size,single ./ node4,'k-+',size,single ./ node6,'r-o',size,single ./ node8,'b-*');
legend('4 nodes','6 nodes','8 nodes');
xlabel('Number of ELMs');
ylabel('Speadup');
title('Speedup of Multiple ELMs Training on Hadoop Cluster');