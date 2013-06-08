single = load('exp2_result1_2.data');
node4 = load('exp2_result2_2.data');
node6 = load('exp2_result3_2.data');
node8 = load('exp2_result4_2.data');
size=load('exp2_result0_2.data');

plot(size/150,node4/54.496248,'k-+',size/150,node6/50.1197593333,'r-o',size/150,node8/52.4112216667,'b-*');
legend('4 nodes','6 nodes','8 nodes');
xlabel('Sizeup of Training ELMs');
ylabel('Sizeup');
title('Sizeup of Voting based Combination on Hadoop Cluster');